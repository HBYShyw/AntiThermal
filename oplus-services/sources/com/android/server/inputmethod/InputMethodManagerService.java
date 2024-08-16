package com.android.server.inputmethod;

import android.R;
import android.annotation.EnforcePermission;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Matrix;
import android.hardware.display.DisplayManagerInternal;
import android.hardware.input.InputManager;
import android.media.AudioManagerInternal;
import android.net.Uri;
import android.os.Binder;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.ShellCommand;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.EventLog;
import android.util.IndentingPrintWriter;
import android.util.IntArray;
import android.util.Pair;
import android.util.PrintWriterPrinter;
import android.util.Printer;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.proto.ProtoOutputStream;
import android.view.DisplayInfo;
import android.view.InputChannel;
import android.view.InputDevice;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ImeTracker;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.window.ImeOnBackInvokedDispatcher;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.content.PackageMonitor;
import com.android.internal.infra.AndroidFuture;
import com.android.internal.inputmethod.IAccessibilityInputMethodSession;
import com.android.internal.inputmethod.IImeTracker;
import com.android.internal.inputmethod.IInlineSuggestionsRequestCallback;
import com.android.internal.inputmethod.IInputContentUriToken;
import com.android.internal.inputmethod.IInputMethod;
import com.android.internal.inputmethod.IInputMethodClient;
import com.android.internal.inputmethod.IInputMethodPrivilegedOperations;
import com.android.internal.inputmethod.IInputMethodSession;
import com.android.internal.inputmethod.IInputMethodSessionCallback;
import com.android.internal.inputmethod.IRemoteAccessibilityInputConnection;
import com.android.internal.inputmethod.IRemoteInputConnection;
import com.android.internal.inputmethod.ImeTracing;
import com.android.internal.inputmethod.InlineSuggestionsRequestInfo;
import com.android.internal.inputmethod.InputBindResult;
import com.android.internal.inputmethod.InputMethodDebug;
import com.android.internal.inputmethod.InputMethodSubtypeHandle;
import com.android.internal.os.TransferPipe;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.ConcurrentUtils;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.internal.view.IInputMethodManager;
import com.android.server.AccessibilityManagerInternal;
import com.android.server.LocalServices;
import com.android.server.ServiceThread;
import com.android.server.SystemServerInitThreadPool;
import com.android.server.SystemService;
import com.android.server.companion.virtual.VirtualDeviceManagerInternal;
import com.android.server.input.InputManagerInternal;
import com.android.server.inputmethod.HandwritingModeController;
import com.android.server.inputmethod.IInputMethodManagerServiceExt;
import com.android.server.inputmethod.ImeVisibilityStateComputer;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.inputmethod.InputMethodSubtypeSwitchingController;
import com.android.server.inputmethod.InputMethodUtils;
import com.android.server.pm.UserManagerInternal;
import com.android.server.pm.verify.domain.DomainVerificationLegacySettings;
import com.android.server.slice.SliceClientPermissions;
import com.android.server.statusbar.StatusBarManagerInternal;
import com.android.server.timezonedetector.ServiceConfigAccessor;
import com.android.server.utils.PriorityDump;
import com.android.server.wm.WindowManagerInternal;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class InputMethodManagerService extends IInputMethodManager.Stub implements Handler.Callback {
    static final int FALLBACK_DISPLAY_ID = 0;
    private static final String HANDLER_THREAD_NAME = "android.imms";
    private static final int MSG_DISPATCH_ON_INPUT_METHOD_LIST_UPDATED = 5010;
    private static final int MSG_FINISH_HANDWRITING = 1110;
    private static final int MSG_HARD_KEYBOARD_SWITCH_CHANGED = 4000;
    private static final int MSG_HIDE_CURRENT_INPUT_METHOD = 1035;
    private static final int MSG_NOTIFY_IME_UID_TO_AUDIO_SERVICE = 7000;
    private static final int MSG_PREPARE_HANDWRITING_DELEGATION = 1130;
    private static final int MSG_REMOVE_HANDWRITING_WINDOW = 1120;
    private static final int MSG_REMOVE_IME_SURFACE = 1060;
    private static final int MSG_REMOVE_IME_SURFACE_FROM_WINDOW = 1061;
    private static final int MSG_RESET_HANDWRITING = 1090;
    private static final int MSG_SET_INTERACTIVE = 3030;
    private static final int MSG_SHOW_IM_SUBTYPE_PICKER = 1;
    private static final int MSG_START_HANDWRITING = 1100;
    private static final int MSG_SYSTEM_UNLOCK_USER = 5000;
    private static final int MSG_UPDATE_IME_WINDOW_STATUS = 1070;
    private static final int NOT_A_SUBTYPE_ID = -1;
    public static final String PROTO_ARG = "--proto";
    private static final String TAG_TRY_SUPPRESSING_IME_SWITCHER = "TrySuppressingImeSwitcher";
    String TAG;
    private final ActivityManagerInternal mActivityManagerInternal;
    private final ArrayMap<String, List<InputMethodSubtype>> mAdditionalSubtypeMap;
    private AudioManagerInternal mAudioManagerInternal;
    private final AutofillSuggestionsController mAutofillController;
    int mBackDisposition;
    private final InputMethodBindingController mBindingController;
    boolean mBoundToAccessibility;
    boolean mBoundToMethod;

    @GuardedBy({"ImfLock.class"})
    final ArrayMap<IBinder, ClientState> mClients;
    final Context mContext;
    private ClientState mCurClient;
    EditorInfo mCurEditorInfo;
    IBinder mCurFocusedWindow;
    ClientState mCurFocusedWindowClient;
    EditorInfo mCurFocusedWindowEditorInfo;
    int mCurFocusedWindowSoftInputMode;

    @GuardedBy({"ImfLock.class"})
    private IBinder mCurHostInputToken;
    ImeOnBackInvokedDispatcher mCurImeDispatcher;
    IRemoteInputConnection mCurInputConnection;
    private boolean mCurPerceptible;
    IRemoteAccessibilityInputConnection mCurRemoteAccessibilityInputConnection;
    private ImeTracker.Token mCurStatsToken;

    @GuardedBy({"ImfLock.class"})
    private int mCurTokenDisplayId;
    private Matrix mCurVirtualDisplayToScreenMatrix;
    private InputMethodSubtype mCurrentSubtype;

    @GuardedBy({"ImfLock.class"})
    private int mDisplayIdToShowIme;
    private final DisplayManagerInternal mDisplayManagerInternal;
    SparseArray<AccessibilitySessionState> mEnabledAccessibilitySessions;

    @GuardedBy({"ImfLock.class"})
    SessionState mEnabledSession;
    private final Handler mHandler;
    final HardwareKeyboardShortcutController mHardwareKeyboardShortcutController;

    @GuardedBy({"ImfLock.class"})
    private final HandwritingModeController mHwController;

    @GuardedBy({"ImfLock.class"})
    private OverlayableSystemBooleanResourceWrapper mImeDrawsImeNavBarRes;

    @GuardedBy({"ImfLock.class"})
    Future<?> mImeDrawsImeNavBarResLazyInitFuture;
    final ImePlatformCompatUtils mImePlatformCompatUtils;

    @GuardedBy({"ImfLock.class"})
    private final WeakHashMap<IBinder, IBinder> mImeTargetWindowMap;
    private final ImeTrackerService mImeTrackerService;
    int mImeWindowVis;
    private InputMethodManagerServiceWrapper mImmsWrapper;
    boolean mInFullscreenMode;
    final InputManagerInternal mInputManagerInternal;
    final InputMethodDeviceConfigs mInputMethodDeviceConfigs;
    private final CopyOnWriteArrayList<InputMethodManagerInternal.InputMethodListListener> mInputMethodListListeners;
    boolean mIsInteractive;
    IBinder mLastImeTargetWindow;
    private int mLastSwitchUserId;
    private LocaleList mLastSystemLocales;
    private final SparseBooleanArray mLoggedDeniedGetInputMethodWindowVisibleHeightForUid;
    private final InputMethodMenuController mMenuController;
    final ArrayList<InputMethodInfo> mMethodList;
    final ArrayMap<String, InputMethodInfo> mMethodMap;

    @GuardedBy({"ImfLock.class"})
    private int mMethodMapUpdateCount;
    private final MyPackageMonitor mMyPackageMonitor;
    private final String[] mNonPreemptibleInputMethods;
    final PackageManagerInternal mPackageManagerInternal;
    private final boolean mPreventImeStartupUnlessTextEditor;
    private final PriorityDump.PriorityDumper mPriorityDumper;
    final Resources mRes;
    final InputMethodUtils.InputMethodSettings mSettings;
    final SettingsObserver mSettingsObserver;
    private boolean mShowOngoingImeSwitcherForPhones;
    private final String mSlotIme;

    @GuardedBy({"ImfLock.class"})
    private final SoftInputShowHideHistory mSoftInputShowHideHistory;

    @GuardedBy({"ImfLock.class"})
    private final StartInputHistory mStartInputHistory;
    private StatusBarManagerInternal mStatusBarManagerInternal;

    @GuardedBy({"ImfLock.class"})
    private IntArray mStylusIds;
    final InputMethodSubtypeSwitchingController mSwitchingController;
    boolean mSystemReady;
    private final UserManagerInternal mUserManagerInternal;

    @GuardedBy({"ImfLock.class"})
    private UserSwitchHandlerTask mUserSwitchHandlerTask;
    private VirtualDeviceManagerInternal mVdmInternal;

    @GuardedBy({"ImfLock.class"})
    private final SparseArray<VirtualDisplayInfo> mVirtualDisplayIdToParentMap;

    @GuardedBy({"ImfLock.class"})
    private final DefaultImeVisibilityApplier mVisibilityApplier;

    @GuardedBy({"ImfLock.class"})
    private final ImeVisibilityStateComputer mVisibilityStateComputer;
    final WindowManagerInternal mWindowManagerInternal;
    private static final Integer VIRTUAL_STYLUS_ID_FOR_TEST = 999999;
    static boolean DEBUG = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    @FunctionalInterface
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ImeDisplayValidator {
        @WindowManager.DisplayImePolicy
        int getDisplayImePolicy(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private @interface ShellCommandResult {
        public static final int FAILURE = -1;
        public static final int SUCCESS = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public int getDisplayIdToShowImeLocked() {
        return this.mDisplayIdToShowIme;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class SessionState {
        InputChannel mChannel;
        final ClientState mClient;
        final IInputMethodInvoker mMethod;
        IInputMethodSession mSession;

        public String toString() {
            return "SessionState{uid " + this.mClient.mUid + " pid " + this.mClient.mPid + " method " + Integer.toHexString(IInputMethodInvoker.getBinderIdentityHashCode(this.mMethod)) + " session " + Integer.toHexString(System.identityHashCode(this.mSession)) + " channel " + this.mChannel + "}";
        }

        SessionState(ClientState clientState, IInputMethodInvoker iInputMethodInvoker, IInputMethodSession iInputMethodSession, InputChannel inputChannel) {
            this.mClient = clientState;
            this.mMethod = iInputMethodInvoker;
            this.mSession = iInputMethodSession;
            this.mChannel = inputChannel;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class AccessibilitySessionState {
        final ClientState mClient;
        final int mId;
        public IAccessibilityInputMethodSession mSession;

        public String toString() {
            return "AccessibilitySessionState{uid " + this.mClient.mUid + " pid " + this.mClient.mPid + " id " + Integer.toHexString(this.mId) + " session " + Integer.toHexString(System.identityHashCode(this.mSession)) + "}";
        }

        AccessibilitySessionState(ClientState clientState, int i, IAccessibilityInputMethodSession iAccessibilityInputMethodSession) {
            this.mClient = clientState;
            this.mId = i;
            this.mSession = iAccessibilityInputMethodSession;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ClientDeathRecipient implements IBinder.DeathRecipient {
        private final IInputMethodClient mClient;
        private final InputMethodManagerService mImms;

        ClientDeathRecipient(InputMethodManagerService inputMethodManagerService, IInputMethodClient iInputMethodClient) {
            this.mImms = inputMethodManagerService;
            this.mClient = iInputMethodClient;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.mImms.removeClient(this.mClient);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ClientState {
        SparseArray<AccessibilitySessionState> mAccessibilitySessions = new SparseArray<>();
        final InputBinding mBinding;
        final IInputMethodClientInvoker mClient;
        final ClientDeathRecipient mClientDeathRecipient;
        SessionState mCurSession;
        final IRemoteInputConnection mFallbackInputConnection;
        final int mPid;
        final int mSelfReportedDisplayId;
        boolean mSessionRequested;
        boolean mSessionRequestedForAccessibility;
        final int mUid;

        public String toString() {
            return "ClientState{" + Integer.toHexString(System.identityHashCode(this)) + " mUid=" + this.mUid + " mPid=" + this.mPid + " mSelfReportedDisplayId=" + this.mSelfReportedDisplayId + "}";
        }

        ClientState(IInputMethodClientInvoker iInputMethodClientInvoker, IRemoteInputConnection iRemoteInputConnection, int i, int i2, int i3, ClientDeathRecipient clientDeathRecipient) {
            this.mClient = iInputMethodClientInvoker;
            this.mFallbackInputConnection = iRemoteInputConnection;
            this.mUid = i;
            this.mPid = i2;
            this.mSelfReportedDisplayId = i3;
            this.mBinding = new InputBinding(null, iRemoteInputConnection.asBinder(), i, i2);
            this.mClientDeathRecipient = clientDeathRecipient;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class VirtualDisplayInfo {
        private final Matrix mMatrix;
        private final ClientState mParentClient;

        VirtualDisplayInfo(ClientState clientState, Matrix matrix) {
            this.mParentClient = clientState;
            this.mMatrix = matrix;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public String getSelectedMethodIdLocked() {
        return this.mBindingController.getSelectedMethodId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public void setSelectedMethodIdLocked(String str) {
        this.mBindingController.setSelectedMethodId(this.mImmsWrapper.getExtImpl().onSetSelectedMethodId(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public int getSequenceNumberLocked() {
        return this.mBindingController.getSequenceNumber();
    }

    @GuardedBy({"ImfLock.class"})
    private void advanceSequenceNumberLocked() {
        this.mBindingController.advanceSequenceNumber();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public String getCurIdLocked() {
        return this.mBindingController.getCurId();
    }

    @GuardedBy({"ImfLock.class"})
    private boolean hasConnectionLocked() {
        return this.mBindingController.hasConnection();
    }

    @GuardedBy({"ImfLock.class"})
    private Intent getCurIntentLocked() {
        return this.mBindingController.getCurIntent();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public IBinder getCurTokenLocked() {
        return this.mBindingController.getCurToken();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public int getCurTokenDisplayIdLocked() {
        return this.mCurTokenDisplayId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void setCurTokenDisplayIdLocked(int i) {
        this.mCurTokenDisplayId = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public IInputMethodInvoker getCurMethodLocked() {
        return this.mBindingController.getCurMethod();
    }

    @GuardedBy({"ImfLock.class"})
    private int getCurMethodUidLocked() {
        return this.mBindingController.getCurMethodUid();
    }

    @GuardedBy({"ImfLock.class"})
    private long getLastBindTimeLocked() {
        return this.mBindingController.getLastBindTime();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class StartInputInfo {
        private static final AtomicInteger sSequenceNumber = new AtomicInteger(0);
        final int mClientBindSequenceNumber;
        final EditorInfo mEditorInfo;
        final int mImeDisplayId;
        final String mImeId;
        final IBinder mImeToken;
        final int mImeUserId;
        final boolean mRestarting;
        final int mStartInputReason;
        final int mTargetDisplayId;
        final int mTargetUserId;
        final IBinder mTargetWindow;
        final int mTargetWindowSoftInputMode;
        final int mSequenceNumber = sSequenceNumber.getAndIncrement();
        final long mTimestamp = SystemClock.uptimeMillis();
        final long mWallTime = System.currentTimeMillis();

        StartInputInfo(int i, IBinder iBinder, int i2, String str, int i3, boolean z, int i4, int i5, IBinder iBinder2, EditorInfo editorInfo, int i6, int i7) {
            this.mImeUserId = i;
            this.mImeToken = iBinder;
            this.mImeDisplayId = i2;
            this.mImeId = str;
            this.mStartInputReason = i3;
            this.mRestarting = z;
            this.mTargetUserId = i4;
            this.mTargetDisplayId = i5;
            this.mTargetWindow = iBinder2;
            this.mEditorInfo = editorInfo;
            this.mTargetWindowSoftInputMode = i6;
            this.mClientBindSequenceNumber = i7;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class SoftInputShowHideHistory {
        private static final AtomicInteger sSequenceNumber = new AtomicInteger(0);
        private final Entry[] mEntries = new Entry[16];
        private int mNextIndex = 0;

        SoftInputShowHideHistory() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public static final class Entry {
            final ClientState mClientState;
            final EditorInfo mEditorInfo;
            final String mFocusedWindowName;
            final int mFocusedWindowSoftInputMode;
            final String mImeControlTargetName;
            final String mImeSurfaceParentName;
            final String mImeTargetNameFromWm;
            final boolean mInFullscreenMode;
            final int mReason;
            final String mRequestWindowName;
            final int mSequenceNumber = SoftInputShowHideHistory.sSequenceNumber.getAndIncrement();
            final long mTimestamp = SystemClock.uptimeMillis();
            final long mWallTime = System.currentTimeMillis();

            Entry(ClientState clientState, EditorInfo editorInfo, String str, int i, int i2, boolean z, String str2, String str3, String str4, String str5) {
                this.mClientState = clientState;
                this.mEditorInfo = editorInfo;
                this.mFocusedWindowName = str;
                this.mFocusedWindowSoftInputMode = i;
                this.mReason = i2;
                this.mInFullscreenMode = z;
                this.mRequestWindowName = str2;
                this.mImeControlTargetName = str3;
                this.mImeTargetNameFromWm = str4;
                this.mImeSurfaceParentName = str5;
            }
        }

        void addEntry(Entry entry) {
            int i = this.mNextIndex;
            Entry[] entryArr = this.mEntries;
            entryArr[i] = entry;
            this.mNextIndex = (i + 1) % entryArr.length;
        }

        void dump(PrintWriter printWriter, String str) {
            DateTimeFormatter withZone = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).withZone(ZoneId.systemDefault());
            int i = 0;
            while (true) {
                Entry[] entryArr = this.mEntries;
                if (i >= entryArr.length) {
                    return;
                }
                Entry entry = entryArr[(this.mNextIndex + i) % entryArr.length];
                if (entry != null) {
                    printWriter.print(str);
                    printWriter.println("SoftInputShowHideHistory #" + entry.mSequenceNumber + ":");
                    printWriter.print(str);
                    printWriter.println(" time=" + withZone.format(Instant.ofEpochMilli(entry.mWallTime)) + " (timestamp=" + entry.mTimestamp + ")");
                    printWriter.print(str);
                    StringBuilder sb = new StringBuilder();
                    sb.append(" reason=");
                    sb.append(InputMethodDebug.softInputDisplayReasonToString(entry.mReason));
                    printWriter.print(sb.toString());
                    printWriter.println(" inFullscreenMode=" + entry.mInFullscreenMode);
                    printWriter.print(str);
                    printWriter.println(" requestClient=" + entry.mClientState);
                    printWriter.print(str);
                    printWriter.println(" focusedWindowName=" + entry.mFocusedWindowName);
                    printWriter.print(str);
                    printWriter.println(" requestWindowName=" + entry.mRequestWindowName);
                    printWriter.print(str);
                    printWriter.println(" imeControlTargetName=" + entry.mImeControlTargetName);
                    printWriter.print(str);
                    printWriter.println(" imeTargetNameFromWm=" + entry.mImeTargetNameFromWm);
                    printWriter.print(str);
                    printWriter.println(" imeSurfaceParentName=" + entry.mImeSurfaceParentName);
                    printWriter.print(str);
                    printWriter.print(" editorInfo: ");
                    if (entry.mEditorInfo != null) {
                        printWriter.print(" inputType=" + entry.mEditorInfo.inputType);
                        printWriter.print(" privateImeOptions=" + entry.mEditorInfo.privateImeOptions);
                        printWriter.println(" fieldId (viewId)=" + entry.mEditorInfo.fieldId);
                    } else {
                        printWriter.println("null");
                    }
                    printWriter.print(str);
                    printWriter.println(" focusedWindowSoftInputMode=" + InputMethodDebug.softInputModeToString(entry.mFocusedWindowSoftInputMode));
                }
                i++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class StartInputHistory {
        private static final int ENTRY_SIZE_FOR_HIGH_RAM_DEVICE = 32;
        private static final int ENTRY_SIZE_FOR_LOW_RAM_DEVICE = 5;
        private final Entry[] mEntries;
        private int mNextIndex;

        private StartInputHistory() {
            this.mEntries = new Entry[getEntrySize()];
            this.mNextIndex = 0;
        }

        private static int getEntrySize() {
            return ActivityManager.isLowRamDeviceStatic() ? 5 : 32;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public static final class Entry {
            int mClientBindSequenceNumber;
            EditorInfo mEditorInfo;
            int mImeDisplayId;
            String mImeId;
            String mImeTokenString;
            int mImeUserId;
            boolean mRestarting;
            int mSequenceNumber;
            int mStartInputReason;
            int mTargetDisplayId;
            int mTargetUserId;
            int mTargetWindowSoftInputMode;
            String mTargetWindowString;
            long mTimestamp;
            long mWallTime;

            Entry(StartInputInfo startInputInfo) {
                set(startInputInfo);
            }

            void set(StartInputInfo startInputInfo) {
                this.mSequenceNumber = startInputInfo.mSequenceNumber;
                this.mTimestamp = startInputInfo.mTimestamp;
                this.mWallTime = startInputInfo.mWallTime;
                this.mImeUserId = startInputInfo.mImeUserId;
                this.mImeTokenString = String.valueOf(startInputInfo.mImeToken);
                this.mImeDisplayId = startInputInfo.mImeDisplayId;
                this.mImeId = startInputInfo.mImeId;
                this.mStartInputReason = startInputInfo.mStartInputReason;
                this.mRestarting = startInputInfo.mRestarting;
                this.mTargetUserId = startInputInfo.mTargetUserId;
                this.mTargetDisplayId = startInputInfo.mTargetDisplayId;
                this.mTargetWindowString = String.valueOf(startInputInfo.mTargetWindow);
                this.mEditorInfo = startInputInfo.mEditorInfo;
                this.mTargetWindowSoftInputMode = startInputInfo.mTargetWindowSoftInputMode;
                this.mClientBindSequenceNumber = startInputInfo.mClientBindSequenceNumber;
            }
        }

        void addEntry(StartInputInfo startInputInfo) {
            int i = this.mNextIndex;
            Entry[] entryArr = this.mEntries;
            Entry entry = entryArr[i];
            if (entry == null) {
                entryArr[i] = new Entry(startInputInfo);
            } else {
                entry.set(startInputInfo);
            }
            this.mNextIndex = (this.mNextIndex + 1) % this.mEntries.length;
        }

        void dump(PrintWriter printWriter, String str) {
            DateTimeFormatter withZone = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).withZone(ZoneId.systemDefault());
            int i = 0;
            while (true) {
                Entry[] entryArr = this.mEntries;
                if (i >= entryArr.length) {
                    return;
                }
                Entry entry = entryArr[(this.mNextIndex + i) % entryArr.length];
                if (entry != null) {
                    printWriter.print(str);
                    printWriter.println("StartInput #" + entry.mSequenceNumber + ":");
                    printWriter.print(str);
                    printWriter.println(" time=" + withZone.format(Instant.ofEpochMilli(entry.mWallTime)) + " (timestamp=" + entry.mTimestamp + ") reason=" + InputMethodDebug.startInputReasonToString(entry.mStartInputReason) + " restarting=" + entry.mRestarting);
                    printWriter.print(str);
                    StringBuilder sb = new StringBuilder();
                    sb.append(" imeToken=");
                    sb.append(entry.mImeTokenString);
                    sb.append(" [");
                    sb.append(entry.mImeId);
                    sb.append("]");
                    printWriter.print(sb.toString());
                    printWriter.print(" imeUserId=" + entry.mImeUserId);
                    printWriter.println(" imeDisplayId=" + entry.mImeDisplayId);
                    printWriter.print(str);
                    printWriter.println(" targetWin=" + entry.mTargetWindowString + " [" + entry.mEditorInfo.packageName + "] targetUserId=" + entry.mTargetUserId + " targetDisplayId=" + entry.mTargetDisplayId + " clientBindSeq=" + entry.mClientBindSequenceNumber);
                    printWriter.print(str);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(" softInputMode=");
                    sb2.append(InputMethodDebug.softInputModeToString(entry.mTargetWindowSoftInputMode));
                    printWriter.println(sb2.toString());
                    printWriter.print(str);
                    printWriter.println(" inputType=0x" + Integer.toHexString(entry.mEditorInfo.inputType) + " imeOptions=0x" + Integer.toHexString(entry.mEditorInfo.imeOptions) + " fieldId=0x" + Integer.toHexString(entry.mEditorInfo.fieldId) + " fieldName=" + entry.mEditorInfo.fieldName + " actionId=" + entry.mEditorInfo.actionId + " actionLabel=" + ((Object) entry.mEditorInfo.actionLabel));
                }
                i++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class SettingsObserver extends ContentObserver {
        String mLastEnabled;
        boolean mRegistered;
        int mUserId;

        SettingsObserver(Handler handler) {
            super(handler);
            this.mRegistered = false;
            this.mLastEnabled = "";
        }

        @GuardedBy({"ImfLock.class"})
        public void registerContentObserverLocked(int i) {
            if (this.mRegistered && this.mUserId == i) {
                return;
            }
            ContentResolver contentResolver = InputMethodManagerService.this.mContext.getContentResolver();
            if (this.mRegistered) {
                InputMethodManagerService.this.mContext.getContentResolver().unregisterContentObserver(this);
                this.mRegistered = false;
            }
            if (this.mUserId != i) {
                this.mLastEnabled = "";
                this.mUserId = i;
            }
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("default_input_method"), false, this, i);
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("enabled_input_methods"), false, this, i);
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("selected_input_method_subtype"), false, this, i);
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("show_ime_with_hard_keyboard"), false, this, i);
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("accessibility_soft_keyboard_mode"), false, this, i);
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("stylus_handwriting_enabled"), false, this);
            InputMethodManagerService.this.mImmsWrapper.getExtImpl().onServerRegisterContentObserver(this, i);
            this.mRegistered = true;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            Uri uriFor = Settings.Secure.getUriFor("show_ime_with_hard_keyboard");
            Uri uriFor2 = Settings.Secure.getUriFor("accessibility_soft_keyboard_mode");
            Uri uriFor3 = Settings.Secure.getUriFor("stylus_handwriting_enabled");
            synchronized (ImfLock.class) {
                if (InputMethodManagerService.this.getWrapper().getExtImpl().onServerSettingsObserverChanged(this, this.mUserId, z, uri)) {
                    return;
                }
                if (uriFor.equals(uri)) {
                    InputMethodManagerService.this.mMenuController.updateKeyboardFromSettingsLocked();
                } else {
                    boolean z2 = false;
                    if (uriFor2.equals(uri)) {
                        InputMethodManagerService.this.mVisibilityStateComputer.getImePolicy().setA11yRequestNoSoftKeyboard(Settings.Secure.getIntForUser(InputMethodManagerService.this.mContext.getContentResolver(), "accessibility_soft_keyboard_mode", 0, this.mUserId));
                        if (InputMethodManagerService.this.mVisibilityStateComputer.getImePolicy().isA11yRequestNoSoftKeyboard()) {
                            InputMethodManagerService inputMethodManagerService = InputMethodManagerService.this;
                            inputMethodManagerService.hideCurrentInputLocked(inputMethodManagerService.mCurFocusedWindow, null, 0, null, 16);
                        } else if (InputMethodManagerService.this.isShowRequestedForCurrentWindow()) {
                            InputMethodManagerService inputMethodManagerService2 = InputMethodManagerService.this;
                            inputMethodManagerService2.showCurrentInputImplicitLocked(inputMethodManagerService2.mCurFocusedWindow, 9);
                        }
                    } else if (uriFor3.equals(uri)) {
                        InputMethodManager.invalidateLocalStylusHandwritingAvailabilityCaches();
                    } else {
                        String enabledInputMethodsStr = InputMethodManagerService.this.mSettings.getEnabledInputMethodsStr();
                        if (!this.mLastEnabled.equals(enabledInputMethodsStr)) {
                            this.mLastEnabled = enabledInputMethodsStr;
                            z2 = true;
                        }
                        InputMethodManagerService.this.updateInputMethodsFromSettingsLocked(z2);
                    }
                }
            }
        }

        public String toString() {
            return "SettingsObserver{mUserId=" + this.mUserId + " mRegistered=" + this.mRegistered + " mLastEnabled=" + this.mLastEnabled + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class ImmsBroadcastReceiverForSystemUser extends BroadcastReceiver {
        private ImmsBroadcastReceiverForSystemUser() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.USER_ADDED".equals(action) || "android.intent.action.USER_REMOVED".equals(action)) {
                InputMethodManagerService.this.updateCurrentProfileIds();
                return;
            }
            if ("android.intent.action.LOCALE_CHANGED".equals(action)) {
                InputMethodManagerService.this.onActionLocaleChanged();
                return;
            }
            Slog.w(InputMethodManagerService.this.TAG, "Unexpected intent " + intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class ImmsBroadcastReceiverForAllUsers extends BroadcastReceiver {
        private ImmsBroadcastReceiverForAllUsers() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                BroadcastReceiver.PendingResult pendingResult = getPendingResult();
                if (pendingResult == null) {
                    return;
                }
                int sendingUserId = pendingResult.getSendingUserId();
                if (sendingUserId == -1 || sendingUserId == InputMethodManagerService.this.mSettings.getCurrentUserId()) {
                    InputMethodManagerService.this.mMenuController.hideInputMethodMenu();
                    return;
                }
                return;
            }
            Slog.w(InputMethodManagerService.this.TAG, "Unexpected intent " + intent);
        }
    }

    void onActionLocaleChanged() {
        synchronized (ImfLock.class) {
            LocaleList locales = this.mRes.getConfiguration().getLocales();
            if (locales == null || !locales.equals(this.mLastSystemLocales)) {
                buildInputMethodListLocked(true);
                resetDefaultImeLocked(this.mContext);
                updateFromSettingsLocked(true);
                this.mLastSystemLocales = locales;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class MyPackageMonitor extends PackageMonitor {

        @GuardedBy({"ImfLock.class"})
        private final ArraySet<String> mKnownImePackageNames = new ArraySet<>();
        private final ArrayList<String> mChangedPackages = new ArrayList<>();
        private boolean mImePackageAppeared = false;

        MyPackageMonitor() {
        }

        @GuardedBy({"ImfLock.class"})
        void clearKnownImePackageNamesLocked() {
            this.mKnownImePackageNames.clear();
        }

        @GuardedBy({"ImfLock.class"})
        void addKnownImePackageNameLocked(String str) {
            this.mKnownImePackageNames.add(str);
        }

        @GuardedBy({"ImfLock.class"})
        private boolean isChangingPackagesOfCurrentUserLocked() {
            int changingUserId = getChangingUserId();
            boolean z = changingUserId == InputMethodManagerService.this.mSettings.getCurrentUserId();
            if (InputMethodManagerService.DEBUG && !z) {
                Slog.d(InputMethodManagerService.this.TAG, "--- ignore this call back from a background user: " + changingUserId);
            }
            return z;
        }

        public boolean onHandleForceStop(Intent intent, String[] strArr, int i, boolean z) {
            synchronized (ImfLock.class) {
                if (!isChangingPackagesOfCurrentUserLocked()) {
                    return false;
                }
                String selectedInputMethod = InputMethodManagerService.this.mSettings.getSelectedInputMethod();
                int size = InputMethodManagerService.this.mMethodList.size();
                if (selectedInputMethod != null) {
                    for (int i2 = 0; i2 < size; i2++) {
                        InputMethodInfo inputMethodInfo = InputMethodManagerService.this.mMethodList.get(i2);
                        if (inputMethodInfo.getId().equals(selectedInputMethod)) {
                            for (String str : strArr) {
                                if (inputMethodInfo.getPackageName().equals(str)) {
                                    if (!z) {
                                        return true;
                                    }
                                    InputMethodManagerService.this.resetSelectedInputMethodAndSubtypeLocked("");
                                    InputMethodManagerService.this.chooseNewDefaultIMELocked();
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }
        }

        public void onBeginPackageChanges() {
            clearPackageChangeState();
        }

        public void onPackageAppeared(String str, int i) {
            if (!this.mImePackageAppeared && !InputMethodManagerService.this.mContext.getPackageManager().queryIntentServicesAsUser(new Intent("android.view.InputMethod").setPackage(str), 512, getChangingUserId()).isEmpty()) {
                this.mImePackageAppeared = true;
            }
            this.mChangedPackages.add(str);
        }

        public void onPackageDisappeared(String str, int i) {
            this.mChangedPackages.add(str);
        }

        public void onPackageModified(String str) {
            this.mChangedPackages.add(str);
        }

        public void onPackagesSuspended(String[] strArr) {
            for (String str : strArr) {
                this.mChangedPackages.add(str);
            }
        }

        public void onPackagesUnsuspended(String[] strArr) {
            for (String str : strArr) {
                this.mChangedPackages.add(str);
            }
        }

        public void onPackageDataCleared(String str, int i) {
            Iterator<InputMethodInfo> it = InputMethodManagerService.this.mMethodList.iterator();
            boolean z = false;
            while (it.hasNext()) {
                InputMethodInfo next = it.next();
                if (next.getPackageName().equals(str)) {
                    InputMethodManagerService.this.mAdditionalSubtypeMap.remove(next.getId());
                    z = true;
                }
            }
            if (z) {
                ArrayMap arrayMap = InputMethodManagerService.this.mAdditionalSubtypeMap;
                InputMethodManagerService inputMethodManagerService = InputMethodManagerService.this;
                AdditionalSubtypeUtils.save(arrayMap, inputMethodManagerService.mMethodMap, inputMethodManagerService.mSettings.getCurrentUserId());
                this.mChangedPackages.add(str);
            }
        }

        public void onFinishPackageChanges() {
            onFinishPackageChangesInternal();
            clearPackageChangeState();
        }

        public void onUidRemoved(int i) {
            synchronized (ImfLock.class) {
                InputMethodManagerService.this.mLoggedDeniedGetInputMethodWindowVisibleHeightForUid.delete(i);
            }
        }

        private void clearPackageChangeState() {
            this.mChangedPackages.clear();
            this.mImePackageAppeared = false;
        }

        @GuardedBy({"ImfLock.class"})
        private boolean shouldRebuildInputMethodListLocked() {
            if (this.mImePackageAppeared) {
                return true;
            }
            int size = this.mChangedPackages.size();
            for (int i = 0; i < size; i++) {
                if (this.mKnownImePackageNames.contains(this.mChangedPackages.get(i))) {
                    return true;
                }
            }
            return false;
        }

        /* JADX WARN: Removed duplicated region for block: B:45:0x0128 A[Catch: all -> 0x015b, TryCatch #1 {, blocks: (B:4:0x0003, B:6:0x0009, B:9:0x000b, B:11:0x0011, B:13:0x0013, B:17:0x002e, B:20:0x0043, B:25:0x0052, B:27:0x00b4, B:29:0x008f, B:32:0x00b8, B:34:0x00bf, B:37:0x00cb, B:39:0x00d9, B:41:0x00eb, B:43:0x0112, B:45:0x0128, B:47:0x013f, B:48:0x0144, B:49:0x0159, B:52:0x0131), top: B:3:0x0003 }] */
        /* JADX WARN: Removed duplicated region for block: B:47:0x013f A[Catch: all -> 0x015b, TryCatch #1 {, blocks: (B:4:0x0003, B:6:0x0009, B:9:0x000b, B:11:0x0011, B:13:0x0013, B:17:0x002e, B:20:0x0043, B:25:0x0052, B:27:0x00b4, B:29:0x008f, B:32:0x00b8, B:34:0x00bf, B:37:0x00cb, B:39:0x00d9, B:41:0x00eb, B:43:0x0112, B:45:0x0128, B:47:0x013f, B:48:0x0144, B:49:0x0159, B:52:0x0131), top: B:3:0x0003 }] */
        /* JADX WARN: Removed duplicated region for block: B:51:0x012f  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void onFinishPackageChangesInternal() {
            boolean z;
            int isPackageDisappearing;
            ServiceInfo serviceInfo;
            synchronized (ImfLock.class) {
                if (isChangingPackagesOfCurrentUserLocked()) {
                    if (shouldRebuildInputMethodListLocked()) {
                        String selectedInputMethod = InputMethodManagerService.this.mSettings.getSelectedInputMethod();
                        int size = InputMethodManagerService.this.mMethodList.size();
                        InputMethodInfo inputMethodInfo = null;
                        boolean z2 = true;
                        InputMethodInfo inputMethodInfo2 = null;
                        if (selectedInputMethod != null) {
                            for (int i = 0; i < size; i++) {
                                InputMethodInfo inputMethodInfo3 = InputMethodManagerService.this.mMethodList.get(i);
                                if (inputMethodInfo3.getId().equals(selectedInputMethod)) {
                                    inputMethodInfo2 = inputMethodInfo3;
                                }
                                int isPackageDisappearing2 = isPackageDisappearing(inputMethodInfo3.getPackageName());
                                if (isPackageDisappearing2 != 2 && isPackageDisappearing2 != 3) {
                                    if (isPackageDisappearing2 == 1) {
                                        Slog.i(InputMethodManagerService.this.TAG, "Input method reinstalling, clearing additional subtypes: " + inputMethodInfo3.getComponent());
                                        InputMethodManagerService.this.mAdditionalSubtypeMap.remove(inputMethodInfo3.getId());
                                        ArrayMap arrayMap = InputMethodManagerService.this.mAdditionalSubtypeMap;
                                        InputMethodManagerService inputMethodManagerService = InputMethodManagerService.this;
                                        AdditionalSubtypeUtils.save(arrayMap, inputMethodManagerService.mMethodMap, inputMethodManagerService.mSettings.getCurrentUserId());
                                    }
                                }
                                Slog.i(InputMethodManagerService.this.TAG, "Input method uninstalled, disabling: " + inputMethodInfo3.getComponent());
                                InputMethodManagerService.this.setInputMethodEnabledLocked(inputMethodInfo3.getId(), false);
                            }
                        }
                        InputMethodManagerService.this.buildInputMethodListLocked(false);
                        if (inputMethodInfo2 != null && ((isPackageDisappearing = isPackageDisappearing(inputMethodInfo2.getPackageName())) == 2 || isPackageDisappearing == 3)) {
                            InputMethodManagerService inputMethodManagerService2 = InputMethodManagerService.this;
                            try {
                                serviceInfo = InputMethodManagerService.getPackageManagerForUser(inputMethodManagerService2.mContext, inputMethodManagerService2.mSettings.getCurrentUserId()).getServiceInfo(inputMethodInfo2.getComponent(), PackageManager.ComponentInfoFlags.of(0L));
                            } catch (PackageManager.NameNotFoundException unused) {
                                serviceInfo = null;
                            }
                            if (serviceInfo == null) {
                                Slog.i(InputMethodManagerService.this.TAG, "Current input method removed: " + selectedInputMethod);
                                InputMethodManagerService inputMethodManagerService3 = InputMethodManagerService.this;
                                inputMethodManagerService3.updateSystemUiLocked(0, inputMethodManagerService3.mBackDisposition);
                                if (!InputMethodManagerService.this.chooseNewDefaultIMELocked()) {
                                    Slog.i(InputMethodManagerService.this.TAG, "Unsetting current input method");
                                    InputMethodManagerService.this.resetSelectedInputMethodAndSubtypeLocked("");
                                    z = true;
                                    if (inputMethodInfo != null) {
                                        z2 = InputMethodManagerService.this.chooseNewDefaultIMELocked();
                                    } else if (z || !isPackageModified(inputMethodInfo.getPackageName())) {
                                        z2 = z;
                                    }
                                    if (z2) {
                                        InputMethodManagerService.this.updateFromSettingsLocked(false);
                                    }
                                    InputMethodManagerService.this.mImmsWrapper.getExtImpl().onFinishPackageChanges(InputMethodManagerService.this.mSettings.getCurrentUserId());
                                }
                            }
                        }
                        z = false;
                        inputMethodInfo = inputMethodInfo2;
                        if (inputMethodInfo != null) {
                        }
                        if (z2) {
                        }
                        InputMethodManagerService.this.mImmsWrapper.getExtImpl().onFinishPackageChanges(InputMethodManagerService.this.mSettings.getCurrentUserId());
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class UserSwitchHandlerTask implements Runnable {
        IInputMethodClientInvoker mClientToBeReset;
        final InputMethodManagerService mService;
        final int mToUserId;

        UserSwitchHandlerTask(InputMethodManagerService inputMethodManagerService, int i, IInputMethodClientInvoker iInputMethodClientInvoker) {
            this.mService = inputMethodManagerService;
            this.mToUserId = i;
            this.mClientToBeReset = iInputMethodClientInvoker;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (ImfLock.class) {
                if (this.mService.mUserSwitchHandlerTask != this) {
                    return;
                }
                InputMethodManagerService inputMethodManagerService = this.mService;
                inputMethodManagerService.switchUserOnHandlerLocked(inputMethodManagerService.mUserSwitchHandlerTask.mToUserId, this.mClientToBeReset);
                this.mService.mUserSwitchHandlerTask = null;
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Lifecycle extends SystemService {
        private final InputMethodManagerService mService;

        public Lifecycle(Context context) {
            super(context);
            this.mService = ((IInputMethodManagerServiceExt.ILifecycleExt) ExtLoader.type(IInputMethodManagerServiceExt.ILifecycleExt.class).base(this).create()).initInputMethodManagerService(context);
        }

        public Lifecycle(Context context, InputMethodManagerService inputMethodManagerService) {
            super(context);
            this.mService = inputMethodManagerService;
        }

        public void onStart() {
            this.mService.publishLocalService();
            publishBinderService("input_method", this.mService, false, 21);
        }

        public void onUserSwitching(SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2) {
            synchronized (ImfLock.class) {
                this.mService.scheduleSwitchUserTaskLocked(targetUser2.getUserIdentifier(), null);
            }
        }

        public void onBootPhase(int i) {
            if (i == 550) {
                this.mService.systemRunning();
            }
        }

        public void onUserUnlocking(SystemService.TargetUser targetUser) {
            this.mService.mHandler.obtainMessage(5000, targetUser.getUserIdentifier(), 0).sendToTarget();
        }
    }

    void onUnlockUser(int i) {
        synchronized (ImfLock.class) {
            int currentUserId = this.mSettings.getCurrentUserId();
            if (DEBUG) {
                Slog.d(this.TAG, "onUnlockUser: userId=" + i + " curUserId=" + currentUserId);
            }
            if (i != currentUserId) {
                return;
            }
            this.mSettings.switchCurrentUser(currentUserId, !this.mSystemReady);
            if (this.mSystemReady) {
                buildInputMethodListLocked(false);
                updateInputMethodsFromSettingsLocked(true);
            }
        }
    }

    @GuardedBy({"ImfLock.class"})
    void scheduleSwitchUserTaskLocked(int i, IInputMethodClientInvoker iInputMethodClientInvoker) {
        UserSwitchHandlerTask userSwitchHandlerTask = this.mUserSwitchHandlerTask;
        if (userSwitchHandlerTask != null) {
            if (userSwitchHandlerTask.mToUserId == i) {
                userSwitchHandlerTask.mClientToBeReset = iInputMethodClientInvoker;
                return;
            }
            this.mHandler.removeCallbacks(userSwitchHandlerTask);
        }
        hideCurrentInputLocked(this.mCurFocusedWindow, null, 0, null, 10);
        UserSwitchHandlerTask userSwitchHandlerTask2 = new UserSwitchHandlerTask(this, i, iInputMethodClientInvoker);
        this.mUserSwitchHandlerTask = userSwitchHandlerTask2;
        this.mHandler.post(userSwitchHandlerTask2);
    }

    public InputMethodManagerService(Context context) {
        this(context, null, null);
    }

    @VisibleForTesting
    InputMethodManagerService(Context context, ServiceThread serviceThread, InputMethodBindingController inputMethodBindingController) {
        this.TAG = "InputMethodManagerService";
        this.mLoggedDeniedGetInputMethodWindowVisibleHeightForUid = new SparseBooleanArray(0);
        ArrayMap<String, List<InputMethodSubtype>> arrayMap = new ArrayMap<>();
        this.mAdditionalSubtypeMap = arrayMap;
        byte b = 0;
        this.mAudioManagerInternal = null;
        this.mVdmInternal = null;
        this.mMethodList = new ArrayList<>();
        ArrayMap<String, InputMethodInfo> arrayMap2 = new ArrayMap<>();
        this.mMethodMap = arrayMap2;
        HardwareKeyboardShortcutController hardwareKeyboardShortcutController = new HardwareKeyboardShortcutController();
        this.mHardwareKeyboardShortcutController = hardwareKeyboardShortcutController;
        this.mMethodMapUpdateCount = 0;
        this.mDisplayIdToShowIme = -1;
        this.mClients = new ArrayMap<>();
        this.mVirtualDisplayIdToParentMap = new SparseArray<>();
        this.mCurVirtualDisplayToScreenMatrix = null;
        this.mCurTokenDisplayId = -1;
        this.mEnabledAccessibilitySessions = new SparseArray<>();
        this.mIsInteractive = true;
        this.mBackDisposition = 0;
        this.mMyPackageMonitor = new MyPackageMonitor();
        this.mInputMethodListListeners = new CopyOnWriteArrayList<>();
        this.mImeTargetWindowMap = new WeakHashMap<>();
        this.mStartInputHistory = new StartInputHistory();
        this.mSoftInputShowHideHistory = new SoftInputShowHideHistory();
        this.mPriorityDumper = new PriorityDump.PriorityDumper() { // from class: com.android.server.inputmethod.InputMethodManagerService.3
            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dumpCritical(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z) {
                if (z) {
                    dumpAsProtoNoCheck(fileDescriptor);
                } else {
                    InputMethodManagerService.this.dumpAsStringNoCheck(fileDescriptor, printWriter, strArr, true);
                }
            }

            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dumpHigh(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z) {
                dumpNormal(fileDescriptor, printWriter, strArr, z);
            }

            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dumpNormal(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z) {
                if (z) {
                    dumpAsProtoNoCheck(fileDescriptor);
                } else {
                    InputMethodManagerService.this.dumpAsStringNoCheck(fileDescriptor, printWriter, strArr, false);
                }
            }

            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z) {
                dumpNormal(fileDescriptor, printWriter, strArr, z);
            }

            private void dumpAsProtoNoCheck(FileDescriptor fileDescriptor) {
                ProtoOutputStream protoOutputStream = new ProtoOutputStream(fileDescriptor);
                InputMethodManagerService.this.dumpDebug(protoOutputStream, 1146756268035L);
                protoOutputStream.flush();
            }
        };
        this.mImmsWrapper = new InputMethodManagerServiceWrapper();
        this.mContext = context;
        Resources resources = context.getResources();
        this.mRes = resources;
        ServiceThread serviceThread2 = serviceThread != null ? serviceThread : new ServiceThread(HANDLER_THREAD_NAME, -2, true);
        serviceThread2.start();
        Handler createAsync = Handler.createAsync(serviceThread2.getLooper(), this);
        this.mHandler = createAsync;
        this.mImeTrackerService = new ImeTrackerService(serviceThread != null ? serviceThread.getLooper() : Looper.getMainLooper());
        this.mSettingsObserver = new SettingsObserver(createAsync);
        this.mWindowManagerInternal = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
        ActivityManagerInternal activityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        this.mActivityManagerInternal = activityManagerInternal;
        this.mPackageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        this.mInputManagerInternal = (InputManagerInternal) LocalServices.getService(InputManagerInternal.class);
        this.mImePlatformCompatUtils = new ImePlatformCompatUtils();
        this.mInputMethodDeviceConfigs = new InputMethodDeviceConfigs();
        this.mDisplayManagerInternal = (DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class);
        this.mUserManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        this.mSlotIme = context.getString(17041687);
        this.mShowOngoingImeSwitcherForPhones = false;
        int currentUserId = activityManagerInternal.getCurrentUserId();
        this.mLastSwitchUserId = currentUserId;
        InputMethodUtils.InputMethodSettings inputMethodSettings = new InputMethodUtils.InputMethodSettings(context, arrayMap2, currentUserId, true ^ this.mSystemReady);
        this.mSettings = inputMethodSettings;
        updateCurrentProfileIds();
        AdditionalSubtypeUtils.load(arrayMap, currentUserId);
        this.mSwitchingController = InputMethodSubtypeSwitchingController.createInstanceLocked(inputMethodSettings, context);
        hardwareKeyboardShortcutController.reset(inputMethodSettings);
        this.mMenuController = new InputMethodMenuController(this);
        this.mBindingController = inputMethodBindingController == null ? new InputMethodBindingController(this) : inputMethodBindingController;
        this.mAutofillController = new AutofillSuggestionsController(this);
        this.TAG = this.mImmsWrapper.getExtImpl().getDebugTAG(this.TAG);
        this.mVisibilityStateComputer = new ImeVisibilityStateComputer(this);
        this.mVisibilityApplier = new DefaultImeVisibilityApplier(this);
        this.mPreventImeStartupUnlessTextEditor = resources.getBoolean(R.bool.action_bar_expanded_action_views_exclusive);
        this.mNonPreemptibleInputMethods = resources.getStringArray(R.array.maps_starting_zoom);
        this.mHwController = new HandwritingModeController(serviceThread2.getLooper(), new InkWindowInitializer());
        registerDeviceListenerAndCheckStylusSupport();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class InkWindowInitializer implements Runnable {
        private InkWindowInitializer() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (ImfLock.class) {
                IInputMethodInvoker curMethodLocked = InputMethodManagerService.this.getCurMethodLocked();
                if (curMethodLocked != null) {
                    curMethodLocked.initInkWindow();
                }
            }
        }
    }

    @GuardedBy({"ImfLock.class"})
    private void resetDefaultImeLocked(Context context) {
        String selectedMethodIdLocked = getSelectedMethodIdLocked();
        if (selectedMethodIdLocked == null || this.mMethodMap.get(selectedMethodIdLocked).isSystem()) {
            ArrayList<InputMethodInfo> defaultEnabledImes = InputMethodInfoUtils.getDefaultEnabledImes(context, this.mSettings.getEnabledInputMethodListLocked());
            if (defaultEnabledImes.isEmpty()) {
                Slog.i(this.TAG, "No default found");
                return;
            }
            InputMethodInfo inputMethodInfo = defaultEnabledImes.get(0);
            if (DEBUG) {
                Slog.i(this.TAG, "Default found, using " + inputMethodInfo.getId());
            }
            setSelectedInputMethodAndSubtypeLocked(inputMethodInfo, -1, false);
        }
    }

    @GuardedBy({"ImfLock.class"})
    private void maybeInitImeNavbarConfigLocked(int i) {
        Context createContextAsUser;
        int profileParentId = this.mUserManagerInternal.getProfileParentId(i);
        OverlayableSystemBooleanResourceWrapper overlayableSystemBooleanResourceWrapper = this.mImeDrawsImeNavBarRes;
        if (overlayableSystemBooleanResourceWrapper != null && overlayableSystemBooleanResourceWrapper.getUserId() != profileParentId) {
            this.mImeDrawsImeNavBarRes.close();
            this.mImeDrawsImeNavBarRes = null;
        }
        if (this.mImeDrawsImeNavBarRes == null) {
            if (this.mContext.getUserId() == profileParentId) {
                createContextAsUser = this.mContext;
            } else {
                createContextAsUser = this.mContext.createContextAsUser(UserHandle.of(profileParentId), 0);
            }
            this.mImeDrawsImeNavBarRes = OverlayableSystemBooleanResourceWrapper.create(createContextAsUser, 17891718, this.mHandler, new Consumer() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda12
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    InputMethodManagerService.this.lambda$maybeInitImeNavbarConfigLocked$0((OverlayableSystemBooleanResourceWrapper) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeInitImeNavbarConfigLocked$0(OverlayableSystemBooleanResourceWrapper overlayableSystemBooleanResourceWrapper) {
        synchronized (ImfLock.class) {
            if (overlayableSystemBooleanResourceWrapper == this.mImeDrawsImeNavBarRes) {
                sendOnNavButtonFlagsChangedLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static PackageManager getPackageManagerForUser(Context context, int i) {
        if (context.getUserId() == i) {
            return context.getPackageManager();
        }
        return context.createContextAsUser(UserHandle.of(i), 0).getPackageManager();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public void switchUserOnHandlerLocked(int i, IInputMethodClientInvoker iInputMethodClientInvoker) {
        ClientState clientState;
        if (DEBUG) {
            Slog.d(this.TAG, "Switching user stage 1/3. newUserId=" + i + " currentUserId=" + this.mSettings.getCurrentUserId());
        }
        maybeInitImeNavbarConfigLocked(i);
        this.mSettingsObserver.registerContentObserverLocked(i);
        this.mSettings.switchCurrentUser(i, (this.mSystemReady && this.mUserManagerInternal.isUserUnlockingOrUnlocked(i)) ? false : true);
        updateCurrentProfileIds();
        AdditionalSubtypeUtils.load(this.mAdditionalSubtypeMap, i);
        String selectedInputMethod = this.mSettings.getSelectedInputMethod();
        if (DEBUG) {
            Slog.d(this.TAG, "Switching user stage 2/3. newUserId=" + i + " defaultImiId=" + selectedInputMethod);
        }
        boolean isEmpty = TextUtils.isEmpty(selectedInputMethod);
        this.mLastSystemLocales = this.mRes.getConfiguration().getLocales();
        resetCurrentMethodAndClientLocked(6);
        buildInputMethodListLocked(isEmpty);
        if (TextUtils.isEmpty(this.mSettings.getSelectedInputMethod())) {
            resetDefaultImeLocked(this.mContext);
        }
        updateFromSettingsLocked(true);
        if (isEmpty) {
            InputMethodUtils.setNonSelectedSystemImesDisabledUntilUsed(getPackageManagerForUser(this.mContext, i), this.mSettings.getEnabledInputMethodListLocked());
        }
        if (DEBUG) {
            Slog.d(this.TAG, "Switching user stage 3/3. newUserId=" + i + " selectedIme=" + this.mSettings.getSelectedInputMethod());
        }
        this.mLastSwitchUserId = i;
        if (!this.mIsInteractive || iInputMethodClientInvoker == null || (clientState = this.mClients.get(iInputMethodClientInvoker.asBinder())) == null) {
            return;
        }
        clientState.mClient.scheduleStartInputIfNecessary(this.mInFullscreenMode);
    }

    void updateCurrentProfileIds() {
        InputMethodUtils.InputMethodSettings inputMethodSettings = this.mSettings;
        inputMethodSettings.setCurrentProfileIds(this.mUserManagerInternal.getProfileIds(inputMethodSettings.getCurrentUserId(), false));
    }

    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        try {
            return super.onTransact(i, parcel, parcel2, i2);
        } catch (RuntimeException e) {
            if (!(e instanceof SecurityException)) {
                Slog.wtf(this.TAG, "Input Method Manager Crash", e);
            }
            throw e;
        }
    }

    public void systemRunning() {
        synchronized (ImfLock.class) {
            if (DEBUG) {
                Slog.d(this.TAG, "--- systemReady");
            }
            if (!this.mSystemReady) {
                this.mSystemReady = true;
                this.mLastSystemLocales = this.mRes.getConfiguration().getLocales();
                final int currentUserId = this.mSettings.getCurrentUserId();
                this.mSettings.switchCurrentUser(currentUserId, !this.mUserManagerInternal.isUserUnlockingOrUnlocked(currentUserId));
                this.mStatusBarManagerInternal = (StatusBarManagerInternal) LocalServices.getService(StatusBarManagerInternal.class);
                hideStatusBarIconLocked();
                updateSystemUiLocked(this.mImeWindowVis, this.mBackDisposition);
                this.mShowOngoingImeSwitcherForPhones = this.mRes.getBoolean(17891938);
                boolean isSecureServcie = this.mImmsWrapper.getExtImpl().isSecureServcie();
                Slog.d(this.TAG, "isSecureServcie " + isSecureServcie);
                boolean z = this.mShowOngoingImeSwitcherForPhones & (!isSecureServcie);
                this.mShowOngoingImeSwitcherForPhones = z;
                if (z) {
                    this.mWindowManagerInternal.setOnHardKeyboardStatusChangeListener(new WindowManagerInternal.OnHardKeyboardStatusChangeListener() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda7
                        public final void onHardKeyboardStatusChange(boolean z2) {
                            InputMethodManagerService.this.lambda$systemRunning$1(z2);
                        }
                    });
                }
                this.mImeDrawsImeNavBarResLazyInitFuture = SystemServerInitThreadPool.submit(new Runnable() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        InputMethodManagerService.this.lambda$systemRunning$2(currentUserId);
                    }
                }, "Lazily initialize IMMS#mImeDrawsImeNavBarRes");
                if (!isSecureServcie) {
                    this.mMyPackageMonitor.register(this.mContext, this.mImmsWrapper.getExtImpl().getBackgroundLooper(), UserHandle.ALL, true);
                    this.mSettingsObserver.registerContentObserverLocked(currentUserId);
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.intent.action.USER_ADDED");
                    intentFilter.addAction("android.intent.action.USER_REMOVED");
                    intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
                    this.mContext.registerReceiver(new ImmsBroadcastReceiverForSystemUser(), intentFilter);
                    IntentFilter intentFilter2 = new IntentFilter();
                    intentFilter2.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
                    this.mContext.registerReceiverAsUser(new ImmsBroadcastReceiverForAllUsers(), UserHandle.ALL, intentFilter2, null, null, 2);
                }
                buildInputMethodListLocked(TextUtils.isEmpty(this.mSettings.getSelectedInputMethod()) ^ true ? false : true);
                updateFromSettingsLocked(true);
                InputMethodUtils.setNonSelectedSystemImesDisabledUntilUsed(getPackageManagerForUser(this.mContext, currentUserId), this.mSettings.getEnabledInputMethodListLocked());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemRunning$1(boolean z) {
        this.mHandler.obtainMessage(MSG_HARD_KEYBOARD_SWITCH_CHANGED, z ? 1 : 0, 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemRunning$2(int i) {
        synchronized (ImfLock.class) {
            this.mImeDrawsImeNavBarResLazyInitFuture = null;
            if (i != this.mSettings.getCurrentUserId()) {
                return;
            }
            maybeInitImeNavbarConfigLocked(i);
        }
    }

    @GuardedBy({"ImfLock.class"})
    private boolean calledWithValidTokenLocked(IBinder iBinder) {
        if (iBinder == null) {
            throw new InvalidParameterException("token must not be null.");
        }
        if (iBinder == getCurTokenLocked() || this.mImmsWrapper.getExtImpl().onCalledWithValidTokenLocked(iBinder)) {
            return true;
        }
        Slog.e(this.TAG, "Ignoring " + Debug.getCaller() + " due to an invalid token. uid:" + Binder.getCallingUid() + " token:" + iBinder);
        return false;
    }

    public InputMethodInfo getCurrentInputMethodInfoAsUser(int i) {
        InputMethodInfo queryDefaultInputMethodForUserIdLocked;
        if (UserHandle.getCallingUserId() != i) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (ImfLock.class) {
            queryDefaultInputMethodForUserIdLocked = queryDefaultInputMethodForUserIdLocked(i);
        }
        return queryDefaultInputMethodForUserIdLocked;
    }

    public List<InputMethodInfo> getInputMethodList(int i, int i2) {
        if (UserHandle.getCallingUserId() != i) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (ImfLock.class) {
            int[] resolveUserId = InputMethodUtils.resolveUserId(i, this.mSettings.getCurrentUserId(), null);
            if (resolveUserId.length != 1) {
                return Collections.emptyList();
            }
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return getInputMethodListLocked(resolveUserId[0], i2, callingUid);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public List<InputMethodInfo> getEnabledInputMethodList(int i) {
        if (UserHandle.getCallingUserId() != i) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (ImfLock.class) {
            int[] resolveUserId = InputMethodUtils.resolveUserId(i, this.mSettings.getCurrentUserId(), null);
            if (resolveUserId.length != 1) {
                return Collections.emptyList();
            }
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return getEnabledInputMethodListLocked(resolveUserId[0], callingUid);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public boolean isStylusHandwritingAvailableAsUser(int i) {
        if (UserHandle.getCallingUserId() != i) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (ImfLock.class) {
            boolean z = false;
            if (!isStylusHandwritingEnabled(this.mContext, i)) {
                return false;
            }
            if (i == this.mSettings.getCurrentUserId()) {
                return this.mBindingController.supportsStylusHandwriting();
            }
            ArrayMap<String, InputMethodInfo> queryMethodMapForUser = queryMethodMapForUser(i);
            InputMethodInfo inputMethodInfo = queryMethodMapForUser.get(new InputMethodUtils.InputMethodSettings(this.mContext, queryMethodMapForUser, i, true).getSelectedInputMethod());
            if (inputMethodInfo != null && inputMethodInfo.supportsStylusHandwriting()) {
                z = true;
            }
            return z;
        }
    }

    private boolean isStylusHandwritingEnabled(Context context, int i) {
        return Settings.Secure.getIntForUser(context.getContentResolver(), "stylus_handwriting_enabled", 1, this.mUserManagerInternal.getProfileParentId(i)) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public List<InputMethodInfo> getInputMethodListLocked(final int i, int i2, final int i3) {
        final InputMethodUtils.InputMethodSettings inputMethodSettings;
        ArrayList<InputMethodInfo> arrayList;
        if (i == this.mSettings.getCurrentUserId() && i2 == 0) {
            arrayList = new ArrayList<>(this.mMethodList);
            inputMethodSettings = this.mSettings;
        } else {
            ArrayMap<String, InputMethodInfo> arrayMap = new ArrayMap<>();
            ArrayList<InputMethodInfo> arrayList2 = new ArrayList<>();
            ArrayMap<String, List<InputMethodSubtype>> arrayMap2 = new ArrayMap<>();
            AdditionalSubtypeUtils.load(arrayMap2, i);
            queryInputMethodServicesInternal(this.mContext, i, arrayMap2, arrayMap, arrayList2, i2, this.mSettings.getEnabledInputMethodNames());
            inputMethodSettings = new InputMethodUtils.InputMethodSettings(this.mContext, arrayMap, i, true);
            arrayList = arrayList2;
        }
        arrayList.removeIf(new Predicate() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getInputMethodListLocked$3;
                lambda$getInputMethodListLocked$3 = InputMethodManagerService.this.lambda$getInputMethodListLocked$3(i3, i, inputMethodSettings, (InputMethodInfo) obj);
                return lambda$getInputMethodListLocked$3;
            }
        });
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getInputMethodListLocked$3(int i, int i2, InputMethodUtils.InputMethodSettings inputMethodSettings, InputMethodInfo inputMethodInfo) {
        return !canCallerAccessInputMethod(inputMethodInfo.getPackageName(), i, i2, inputMethodSettings);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public List<InputMethodInfo> getEnabledInputMethodListLocked(final int i, final int i2) {
        final InputMethodUtils.InputMethodSettings inputMethodSettings;
        ArrayList<InputMethodInfo> enabledInputMethodListLocked;
        if (i == this.mSettings.getCurrentUserId()) {
            enabledInputMethodListLocked = this.mSettings.getEnabledInputMethodListLocked();
            inputMethodSettings = this.mSettings;
        } else {
            inputMethodSettings = new InputMethodUtils.InputMethodSettings(this.mContext, queryMethodMapForUser(i), i, true);
            enabledInputMethodListLocked = inputMethodSettings.getEnabledInputMethodListLocked();
        }
        enabledInputMethodListLocked.removeIf(new Predicate() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getEnabledInputMethodListLocked$4;
                lambda$getEnabledInputMethodListLocked$4 = InputMethodManagerService.this.lambda$getEnabledInputMethodListLocked$4(i2, i, inputMethodSettings, (InputMethodInfo) obj);
                return lambda$getEnabledInputMethodListLocked$4;
            }
        });
        return enabledInputMethodListLocked;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getEnabledInputMethodListLocked$4(int i, int i2, InputMethodUtils.InputMethodSettings inputMethodSettings, InputMethodInfo inputMethodInfo) {
        return !canCallerAccessInputMethod(inputMethodInfo.getPackageName(), i, i2, inputMethodSettings);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void performOnCreateInlineSuggestionsRequestLocked() {
        this.mAutofillController.performOnCreateInlineSuggestionsRequest();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCurHostInputToken(IBinder iBinder, IBinder iBinder2) {
        synchronized (ImfLock.class) {
            if (calledWithValidTokenLocked(iBinder)) {
                this.mCurHostInputToken = iBinder2;
            }
        }
    }

    public List<InputMethodSubtype> getEnabledInputMethodSubtypeList(String str, boolean z, int i) {
        List<InputMethodSubtype> enabledInputMethodSubtypeListLocked;
        if (UserHandle.getCallingUserId() != i) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (ImfLock.class) {
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                enabledInputMethodSubtypeListLocked = getEnabledInputMethodSubtypeListLocked(str, z, i, callingUid);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        return enabledInputMethodSubtypeListLocked;
    }

    @GuardedBy({"ImfLock.class"})
    private List<InputMethodSubtype> getEnabledInputMethodSubtypeListLocked(String str, boolean z, int i, int i2) {
        InputMethodInfo inputMethodInfo;
        if (i == this.mSettings.getCurrentUserId()) {
            String selectedMethodIdLocked = getSelectedMethodIdLocked();
            if (str == null && selectedMethodIdLocked != null) {
                inputMethodInfo = this.mMethodMap.get(selectedMethodIdLocked);
            } else {
                inputMethodInfo = this.mMethodMap.get(str);
            }
            if (inputMethodInfo == null || !canCallerAccessInputMethod(inputMethodInfo.getPackageName(), i2, i, this.mSettings)) {
                return Collections.emptyList();
            }
            return this.mSettings.getEnabledInputMethodSubtypeListLocked(inputMethodInfo, z);
        }
        ArrayMap<String, InputMethodInfo> queryMethodMapForUser = queryMethodMapForUser(i);
        InputMethodInfo inputMethodInfo2 = queryMethodMapForUser.get(str);
        if (inputMethodInfo2 == null) {
            return Collections.emptyList();
        }
        InputMethodUtils.InputMethodSettings inputMethodSettings = new InputMethodUtils.InputMethodSettings(this.mContext, queryMethodMapForUser, i, true);
        if (!canCallerAccessInputMethod(inputMethodInfo2.getPackageName(), i2, i, inputMethodSettings)) {
            return Collections.emptyList();
        }
        return inputMethodSettings.getEnabledInputMethodSubtypeListLocked(inputMethodInfo2, z);
    }

    public void addClient(IInputMethodClient iInputMethodClient, IRemoteInputConnection iRemoteInputConnection, int i) {
        int callingUid = Binder.getCallingUid();
        int callingPid = Binder.getCallingPid();
        synchronized (ImfLock.class) {
            int size = this.mClients.size();
            for (int i2 = 0; i2 < size; i2++) {
                ClientState valueAt = this.mClients.valueAt(i2);
                if (valueAt.mUid == callingUid && valueAt.mPid == callingPid && valueAt.mSelfReportedDisplayId == i) {
                    throw new SecurityException("uid=" + callingUid + "/pid=" + callingPid + "/displayId=" + i + " is already registered.");
                }
            }
            ClientDeathRecipient clientDeathRecipient = new ClientDeathRecipient(this, iInputMethodClient);
            try {
                iInputMethodClient.asBinder().linkToDeath(clientDeathRecipient, 0);
                this.mClients.put(iInputMethodClient.asBinder(), new ClientState(IInputMethodClientInvoker.create(iInputMethodClient, this.mHandler), iRemoteInputConnection, callingUid, callingPid, i, clientDeathRecipient));
            } catch (RemoteException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    void removeClient(IInputMethodClient iInputMethodClient) {
        synchronized (ImfLock.class) {
            ClientState remove = this.mClients.remove(iInputMethodClient.asBinder());
            if (remove != null) {
                iInputMethodClient.asBinder().unlinkToDeath(remove.mClientDeathRecipient, 0);
                clearClientSessionLocked(remove);
                clearClientSessionForAccessibilityLocked(remove);
                for (int size = this.mVirtualDisplayIdToParentMap.size() - 1; size >= 0; size--) {
                    if (this.mVirtualDisplayIdToParentMap.valueAt(size).mParentClient == remove) {
                        this.mVirtualDisplayIdToParentMap.removeAt(size);
                    }
                }
                if (this.mCurClient == remove) {
                    hideCurrentInputLocked(this.mCurFocusedWindow, null, 0, null, 22);
                    if (this.mBoundToMethod) {
                        this.mBoundToMethod = false;
                        IInputMethodInvoker curMethodLocked = getCurMethodLocked();
                        if (curMethodLocked != null) {
                            curMethodLocked.unbindInput();
                            AccessibilityManagerInternal.get().unbindInput();
                        }
                    }
                    this.mBoundToAccessibility = false;
                    this.mCurClient = null;
                    this.mCurVirtualDisplayToScreenMatrix = null;
                }
                if (this.mCurFocusedWindowClient == remove) {
                    this.mCurFocusedWindowClient = null;
                    this.mCurFocusedWindowEditorInfo = null;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void unbindCurrentClientLocked(int i) {
        if (this.mCurClient != null) {
            if (DEBUG) {
                Slog.v(this.TAG, "unbindCurrentInputLocked: client=" + this.mCurClient.mClient.asBinder());
            }
            if (this.mBoundToMethod) {
                this.mBoundToMethod = false;
                IInputMethodInvoker curMethodLocked = getCurMethodLocked();
                if (curMethodLocked != null) {
                    curMethodLocked.unbindInput();
                }
            }
            this.mBoundToAccessibility = false;
            this.mCurClient.mClient.setActive(false, false);
            this.mCurClient.mClient.onUnbindMethod(getSequenceNumberLocked(), i);
            ClientState clientState = this.mCurClient;
            clientState.mSessionRequested = false;
            clientState.mSessionRequestedForAccessibility = false;
            this.mCurClient = null;
            this.mCurVirtualDisplayToScreenMatrix = null;
            ImeTracker.forLogging().onFailed(this.mCurStatsToken, 8);
            this.mCurStatsToken = null;
            InputMethodManager.invalidateLocalStylusHandwritingAvailabilityCaches();
            this.mMenuController.hideInputMethodMenuLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public boolean hasAttachedClient() {
        return this.mCurClient != null;
    }

    @VisibleForTesting
    void setAttachedClientForTesting(ClientState clientState) {
        synchronized (ImfLock.class) {
            this.mCurClient = clientState;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void clearInputShownLocked() {
        this.mVisibilityStateComputer.setInputShown(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public boolean isInputShown() {
        return this.mVisibilityStateComputer.isInputShown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public boolean isShowRequestedForCurrentWindow() {
        ImeVisibilityStateComputer.ImeTargetWindowState windowStateOrNull = this.mVisibilityStateComputer.getWindowStateOrNull(this.mCurFocusedWindow);
        return windowStateOrNull != null && windowStateOrNull.isRequestedImeVisible();
    }

    @GuardedBy({"ImfLock.class"})
    InputBindResult attachNewInputLocked(int i, boolean z) {
        if (!this.mBoundToMethod) {
            getCurMethodLocked().bindInput(this.mCurClient.mBinding);
            this.mBoundToMethod = true;
        }
        boolean z2 = !z;
        Binder binder = new Binder();
        StartInputInfo startInputInfo = new StartInputInfo(this.mSettings.getCurrentUserId(), getCurTokenLocked(), this.mCurTokenDisplayId, getCurIdLocked(), i, z2, UserHandle.getUserId(this.mCurClient.mUid), this.mCurClient.mSelfReportedDisplayId, this.mCurFocusedWindow, this.mCurEditorInfo, this.mCurFocusedWindowSoftInputMode, getSequenceNumberLocked());
        this.mImeTargetWindowMap.put(binder, this.mCurFocusedWindow);
        this.mStartInputHistory.addEntry(startInputInfo);
        if (this.mSettings.getCurrentUserId() == UserHandle.getUserId(this.mCurClient.mUid)) {
            this.mPackageManagerInternal.grantImplicitAccess(this.mSettings.getCurrentUserId(), (Intent) null, UserHandle.getAppId(getCurMethodUidLocked()), this.mCurClient.mUid, true);
        }
        int inputMethodNavButtonFlagsLocked = getInputMethodNavButtonFlagsLocked();
        SessionState sessionState = this.mCurClient.mCurSession;
        setEnabledSessionLocked(sessionState);
        sessionState.mMethod.startInput(binder, this.mCurInputConnection, this.mCurEditorInfo, z2, inputMethodNavButtonFlagsLocked, this.mCurImeDispatcher);
        IInputMethod.StartInputParams startInputParams = new IInputMethod.StartInputParams();
        startInputParams.startInputToken = binder;
        startInputParams.remoteInputConnection = this.mCurInputConnection;
        startInputParams.editorInfo = this.mCurEditorInfo;
        startInputParams.restarting = z2;
        startInputParams.navigationBarFlags = inputMethodNavButtonFlagsLocked;
        startInputParams.imeDispatcher = null;
        this.mImmsWrapper.getExtImpl().startInputToSynergy(startInputParams);
        if (isShowRequestedForCurrentWindow()) {
            if (DEBUG) {
                Slog.v(this.TAG, "Attach new input asks to show input");
            }
            ImeTracker.Token token = this.mCurStatsToken;
            this.mCurStatsToken = null;
            showCurrentInputLocked(this.mCurFocusedWindow, token, this.mVisibilityStateComputer.getImeShowFlags(), null, 2);
        }
        String curIdLocked = getCurIdLocked();
        InputMethodInfo inputMethodInfo = this.mMethodMap.get(curIdLocked);
        boolean z3 = inputMethodInfo != null && inputMethodInfo.suppressesSpellChecker();
        SparseArray<IAccessibilityInputMethodSession> createAccessibilityInputMethodSessions = createAccessibilityInputMethodSessions(this.mCurClient.mAccessibilitySessions);
        if (this.mBindingController.supportsStylusHandwriting() && hasSupportedStylusLocked()) {
            this.mHwController.setInkWindowInitializer(new InkWindowInitializer());
        }
        IInputMethodSession iInputMethodSession = sessionState.mSession;
        InputChannel inputChannel = sessionState.mChannel;
        return new InputBindResult(0, iInputMethodSession, createAccessibilityInputMethodSessions, inputChannel != null ? inputChannel.dup() : null, curIdLocked, getSequenceNumberLocked(), this.mCurVirtualDisplayToScreenMatrix, z3);
    }

    @GuardedBy({"ImfLock.class"})
    private Matrix getVirtualDisplayToScreenMatrixLocked(int i, int i2) {
        if (i == i2) {
            return null;
        }
        Matrix matrix = null;
        while (true) {
            VirtualDisplayInfo virtualDisplayInfo = this.mVirtualDisplayIdToParentMap.get(i);
            if (virtualDisplayInfo == null) {
                return null;
            }
            if (matrix == null) {
                matrix = new Matrix(virtualDisplayInfo.mMatrix);
            } else {
                matrix.postConcat(virtualDisplayInfo.mMatrix);
            }
            if (virtualDisplayInfo.mParentClient.mSelfReportedDisplayId == i2) {
                return matrix;
            }
            i = virtualDisplayInfo.mParentClient.mSelfReportedDisplayId;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public void attachNewAccessibilityLocked(int i, boolean z) {
        if (!this.mBoundToAccessibility) {
            AccessibilityManagerInternal.get().bindInput();
            this.mBoundToAccessibility = true;
        }
        if (i != 11) {
            setEnabledSessionForAccessibilityLocked(this.mCurClient.mAccessibilitySessions);
            AccessibilityManagerInternal.get().startInput(this.mCurRemoteAccessibilityInputConnection, this.mCurEditorInfo, !z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SparseArray<IAccessibilityInputMethodSession> createAccessibilityInputMethodSessions(SparseArray<AccessibilitySessionState> sparseArray) {
        SparseArray<IAccessibilityInputMethodSession> sparseArray2 = new SparseArray<>();
        if (sparseArray != null) {
            for (int i = 0; i < sparseArray.size(); i++) {
                sparseArray2.append(sparseArray.keyAt(i), sparseArray.valueAt(i).mSession);
            }
        }
        return sparseArray2;
    }

    @GuardedBy({"ImfLock.class"})
    private InputBindResult startInputUncheckedLocked(ClientState clientState, IRemoteInputConnection iRemoteInputConnection, IRemoteAccessibilityInputConnection iRemoteAccessibilityInputConnection, EditorInfo editorInfo, int i, int i2, int i3, ImeOnBackInvokedDispatcher imeOnBackInvokedDispatcher) {
        LocaleList preferredLocaleListForUid;
        String selectedMethodIdLocked = getSelectedMethodIdLocked();
        if (selectedMethodIdLocked == null) {
            return InputBindResult.NO_IME;
        }
        if (!this.mSystemReady) {
            return new InputBindResult(8, (IInputMethodSession) null, (SparseArray) null, (InputChannel) null, selectedMethodIdLocked, getSequenceNumberLocked(), (Matrix) null, false);
        }
        if (!InputMethodUtils.checkIfPackageBelongsToUid(this.mPackageManagerInternal, clientState.mUid, editorInfo.packageName)) {
            Slog.e(this.TAG, "Rejecting this client as it reported an invalid package name. uid=" + clientState.mUid + " package=" + editorInfo.packageName);
            return InputBindResult.INVALID_PACKAGE_NAME;
        }
        ImeVisibilityStateComputer.ImeTargetWindowState windowStateOrNull = this.mVisibilityStateComputer.getWindowStateOrNull(this.mCurFocusedWindow);
        if (windowStateOrNull == null) {
            return InputBindResult.NOT_IME_TARGET_WINDOW;
        }
        this.mDisplayIdToShowIme = this.mVisibilityStateComputer.computeImeDisplayId(windowStateOrNull, clientState.mSelfReportedDisplayId);
        if (this.mVisibilityStateComputer.getImePolicy().isImeHiddenByDisplayPolicy()) {
            hideCurrentInputLocked(this.mCurFocusedWindow, null, 0, null, 27);
            return InputBindResult.NO_IME;
        }
        if (this.mCurClient != clientState) {
            prepareClientSwitchLocked(clientState);
        }
        advanceSequenceNumberLocked();
        this.mCurClient = clientState;
        this.mCurInputConnection = iRemoteInputConnection;
        this.mCurRemoteAccessibilityInputConnection = iRemoteAccessibilityInputConnection;
        this.mCurImeDispatcher = imeOnBackInvokedDispatcher;
        this.mCurVirtualDisplayToScreenMatrix = getVirtualDisplayToScreenMatrixLocked(clientState.mSelfReportedDisplayId, this.mDisplayIdToShowIme);
        if (this.mVdmInternal == null) {
            this.mVdmInternal = (VirtualDeviceManagerInternal) LocalServices.getService(VirtualDeviceManagerInternal.class);
        }
        VirtualDeviceManagerInternal virtualDeviceManagerInternal = this.mVdmInternal;
        if (virtualDeviceManagerInternal != null && editorInfo.hintLocales == null && (preferredLocaleListForUid = virtualDeviceManagerInternal.getPreferredLocaleListForUid(clientState.mUid)) != null) {
            editorInfo.hintLocales = preferredLocaleListForUid;
        }
        this.mCurEditorInfo = editorInfo;
        if (shouldPreventImeStartupLocked(selectedMethodIdLocked, i, i3)) {
            if (DEBUG) {
                Slog.d(this.TAG, "Avoiding IME startup and unbinding current input method.");
            }
            invalidateAutofillSessionLocked();
            this.mBindingController.unbindCurrentMethod();
            return InputBindResult.NO_EDITOR;
        }
        if (isSelectedMethodBoundLocked()) {
            if (clientState.mCurSession != null) {
                clientState.mSessionRequestedForAccessibility = false;
                requestClientSessionForAccessibilityLocked(clientState);
                int i4 = i & 4;
                attachNewAccessibilityLocked(i2, i4 != 0);
                return attachNewInputLocked(i2, i4 != 0);
            }
            InputBindResult tryReuseConnectionLocked = tryReuseConnectionLocked(clientState);
            if (tryReuseConnectionLocked != null) {
                return tryReuseConnectionLocked;
            }
        }
        this.mBindingController.unbindCurrentMethod();
        return this.mBindingController.bindCurrentMethod();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void invalidateAutofillSessionLocked() {
        this.mAutofillController.invalidateAutofillSession();
    }

    @GuardedBy({"ImfLock.class"})
    private boolean shouldPreventImeStartupLocked(String str, int i, int i2) {
        InputMethodInfo inputMethodInfo;
        return (!this.mPreventImeStartupUnlessTextEditor || isShowRequestedForCurrentWindow() || InputMethodUtils.isSoftInputModeStateVisibleAllowed(i2, i) || (inputMethodInfo = this.mMethodMap.get(str)) == null || ArrayUtils.contains(this.mNonPreemptibleInputMethods, inputMethodInfo.getPackageName())) ? false : true;
    }

    @GuardedBy({"ImfLock.class"})
    private boolean isSelectedMethodBoundLocked() {
        String curIdLocked = getCurIdLocked();
        return curIdLocked != null && curIdLocked.equals(getSelectedMethodIdLocked()) && this.mDisplayIdToShowIme == this.mCurTokenDisplayId;
    }

    @GuardedBy({"ImfLock.class"})
    private void prepareClientSwitchLocked(ClientState clientState) {
        unbindCurrentClientLocked(1);
        if (this.mIsInteractive) {
            clientState.mClient.setActive(true, false);
        }
    }

    @GuardedBy({"ImfLock.class"})
    private InputBindResult tryReuseConnectionLocked(ClientState clientState) {
        if (!hasConnectionLocked()) {
            return null;
        }
        if (getCurMethodLocked() != null) {
            requestClientSessionLocked(clientState);
            requestClientSessionForAccessibilityLocked(clientState);
            return new InputBindResult(1, (IInputMethodSession) null, (SparseArray) null, (InputChannel) null, getCurIdLocked(), getSequenceNumberLocked(), (Matrix) null, false);
        }
        long uptimeMillis = SystemClock.uptimeMillis() - getLastBindTimeLocked();
        if (uptimeMillis < 3000) {
            return new InputBindResult(2, (IInputMethodSession) null, (SparseArray) null, (InputChannel) null, getCurIdLocked(), getSequenceNumberLocked(), (Matrix) null, false);
        }
        EventLog.writeEvent(32000, getSelectedMethodIdLocked(), Long.valueOf(uptimeMillis), 0);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int computeImeDisplayIdForTarget(int i, ImeDisplayValidator imeDisplayValidator) {
        if (i != 0 && i != -1) {
            int displayImePolicy = imeDisplayValidator.getDisplayImePolicy(i);
            if (displayImePolicy == 0) {
                return i;
            }
            if (displayImePolicy == 2) {
                return -1;
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void initializeImeLocked(IInputMethodInvoker iInputMethodInvoker, IBinder iBinder) {
        if (DEBUG) {
            Slog.v(this.TAG, "Sending attach of token: " + iBinder + " for display: " + this.mCurTokenDisplayId);
        }
        iInputMethodInvoker.initializeInternal(iBinder, new InputMethodPrivilegedOperationsImpl(this, iBinder), getInputMethodNavButtonFlagsLocked());
        this.mImmsWrapper.getExtImpl().onImeInitialized(this.mCurTokenDisplayId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleResetStylusHandwriting() {
        this.mHandler.obtainMessage(MSG_RESET_HANDWRITING).sendToTarget();
    }

    void schedulePrepareStylusHandwritingDelegation(String str, String str2) {
        this.mHandler.obtainMessage(MSG_PREPARE_HANDWRITING_DELEGATION, new Pair(str, str2)).sendToTarget();
    }

    void scheduleRemoveStylusHandwritingWindow() {
        this.mHandler.obtainMessage(MSG_REMOVE_HANDWRITING_WINDOW).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleNotifyImeUidToAudioService(int i) {
        this.mHandler.removeMessages(MSG_NOTIFY_IME_UID_TO_AUDIO_SERVICE);
        this.mHandler.obtainMessage(MSG_NOTIFY_IME_UID_TO_AUDIO_SERVICE, i, 0).sendToTarget();
    }

    void onSessionCreated(IInputMethodInvoker iInputMethodInvoker, IInputMethodSession iInputMethodSession, InputChannel inputChannel) {
        ClientState clientState;
        Trace.traceBegin(32L, "IMMS.onSessionCreated");
        try {
            synchronized (ImfLock.class) {
                if (this.mUserSwitchHandlerTask != null) {
                    inputChannel.dispose();
                    return;
                }
                IInputMethodInvoker curMethodLocked = getCurMethodLocked();
                if (curMethodLocked == null || iInputMethodInvoker == null || curMethodLocked.asBinder() != iInputMethodInvoker.asBinder() || (clientState = this.mCurClient) == null) {
                    inputChannel.dispose();
                    return;
                }
                clearClientSessionLocked(clientState);
                ClientState clientState2 = this.mCurClient;
                clientState2.mCurSession = new SessionState(clientState2, iInputMethodInvoker, iInputMethodSession, inputChannel);
                InputBindResult attachNewInputLocked = attachNewInputLocked(10, true);
                attachNewAccessibilityLocked(10, true);
                if (attachNewInputLocked.method != null) {
                    this.mCurClient.mClient.onBindMethod(attachNewInputLocked);
                }
            }
        } finally {
            Trace.traceEnd(32L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void resetSystemUiLocked() {
        this.mImeWindowVis = 0;
        this.mBackDisposition = 0;
        updateSystemUiLocked(0, 0);
        this.mCurTokenDisplayId = -1;
        this.mCurHostInputToken = null;
    }

    @GuardedBy({"ImfLock.class"})
    void resetCurrentMethodAndClientLocked(int i) {
        setSelectedMethodIdLocked(null);
        this.mBindingController.unbindCurrentMethod();
        unbindCurrentClientLocked(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void reRequestCurrentClientSessionLocked() {
        ClientState clientState = this.mCurClient;
        if (clientState != null) {
            clearClientSessionLocked(clientState);
            clearClientSessionForAccessibilityLocked(this.mCurClient);
            requestClientSessionLocked(this.mCurClient);
            requestClientSessionForAccessibilityLocked(this.mCurClient);
        }
    }

    @GuardedBy({"ImfLock.class"})
    void requestClientSessionLocked(ClientState clientState) {
        if (clientState.mSessionRequested) {
            return;
        }
        if (DEBUG) {
            Slog.v(this.TAG, "Creating new session for client " + clientState);
        }
        InputChannel[] openInputChannelPair = InputChannel.openInputChannelPair(clientState.toString());
        final InputChannel inputChannel = openInputChannelPair[0];
        InputChannel inputChannel2 = openInputChannelPair[1];
        clientState.mSessionRequested = true;
        final IInputMethodInvoker curMethodLocked = getCurMethodLocked();
        try {
            curMethodLocked.createSession(inputChannel2, new IInputMethodSessionCallback.Stub() { // from class: com.android.server.inputmethod.InputMethodManagerService.1
                public void sessionCreated(IInputMethodSession iInputMethodSession) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        InputMethodManagerService.this.onSessionCreated(curMethodLocked, iInputMethodSession, inputChannel);
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            });
        } finally {
            if (inputChannel2 != null) {
                inputChannel2.dispose();
            }
        }
    }

    @GuardedBy({"ImfLock.class"})
    void requestClientSessionForAccessibilityLocked(ClientState clientState) {
        if (clientState.mSessionRequestedForAccessibility) {
            return;
        }
        if (DEBUG) {
            Slog.v(this.TAG, "Creating new accessibility sessions for client " + clientState);
        }
        clientState.mSessionRequestedForAccessibility = true;
        ArraySet arraySet = new ArraySet();
        for (int i = 0; i < clientState.mAccessibilitySessions.size(); i++) {
            arraySet.add(Integer.valueOf(clientState.mAccessibilitySessions.keyAt(i)));
        }
        AccessibilityManagerInternal.get().createImeSession(arraySet);
    }

    @GuardedBy({"ImfLock.class"})
    void clearClientSessionLocked(ClientState clientState) {
        finishSessionLocked(clientState.mCurSession);
        clientState.mCurSession = null;
        clientState.mSessionRequested = false;
    }

    @GuardedBy({"ImfLock.class"})
    void clearClientSessionForAccessibilityLocked(ClientState clientState) {
        for (int i = 0; i < clientState.mAccessibilitySessions.size(); i++) {
            finishSessionForAccessibilityLocked(clientState.mAccessibilitySessions.valueAt(i));
        }
        clientState.mAccessibilitySessions.clear();
        clientState.mSessionRequestedForAccessibility = false;
    }

    @GuardedBy({"ImfLock.class"})
    void clearClientSessionForAccessibilityLocked(ClientState clientState, int i) {
        AccessibilitySessionState accessibilitySessionState = clientState.mAccessibilitySessions.get(i);
        if (accessibilitySessionState != null) {
            finishSessionForAccessibilityLocked(accessibilitySessionState);
            clientState.mAccessibilitySessions.remove(i);
        }
    }

    @GuardedBy({"ImfLock.class"})
    private void finishSessionLocked(SessionState sessionState) {
        if (sessionState != null) {
            IInputMethodSession iInputMethodSession = sessionState.mSession;
            if (iInputMethodSession != null) {
                try {
                    iInputMethodSession.finishSession();
                } catch (RemoteException e) {
                    Slog.w(this.TAG, "Session failed to close due to remote exception", e);
                    updateSystemUiLocked(0, this.mBackDisposition);
                }
                sessionState.mSession = null;
            }
            InputChannel inputChannel = sessionState.mChannel;
            if (inputChannel != null) {
                inputChannel.dispose();
                sessionState.mChannel = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public void finishSessionForAccessibilityLocked(AccessibilitySessionState accessibilitySessionState) {
        IAccessibilityInputMethodSession iAccessibilityInputMethodSession;
        if (accessibilitySessionState == null || (iAccessibilityInputMethodSession = accessibilitySessionState.mSession) == null) {
            return;
        }
        try {
            iAccessibilityInputMethodSession.finishSession();
        } catch (RemoteException e) {
            Slog.w(this.TAG, "Session failed to close due to remote exception", e);
        }
        accessibilitySessionState.mSession = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void clearClientSessionsLocked() {
        if (getCurMethodLocked() != null) {
            int size = this.mClients.size();
            for (int i = 0; i < size; i++) {
                clearClientSessionLocked(this.mClients.valueAt(i));
                clearClientSessionForAccessibilityLocked(this.mClients.valueAt(i));
            }
            finishSessionLocked(this.mEnabledSession);
            for (int i2 = 0; i2 < this.mEnabledAccessibilitySessions.size(); i2++) {
                finishSessionForAccessibilityLocked(this.mEnabledAccessibilitySessions.valueAt(i2));
            }
            this.mEnabledSession = null;
            this.mEnabledAccessibilitySessions.clear();
            scheduleNotifyImeUidToAudioService(-1);
        }
        hideStatusBarIconLocked();
        this.mInFullscreenMode = false;
        this.mWindowManagerInternal.setDismissImeOnBackKeyPressed(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStatusIcon(IBinder iBinder, String str, int i) {
        ApplicationInfo applicationInfo;
        synchronized (ImfLock.class) {
            if (calledWithValidTokenLocked(iBinder)) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (i == 0) {
                        if (DEBUG) {
                            Slog.d(this.TAG, "hide the small icon for the input method");
                        }
                        hideStatusBarIconLocked();
                    } else if (str != null) {
                        if (DEBUG) {
                            Slog.d(this.TAG, "show a small icon for the input method");
                        }
                        PackageManager packageManagerForUser = getPackageManagerForUser(this.mContext, this.mSettings.getCurrentUserId());
                        try {
                            applicationInfo = packageManagerForUser.getApplicationInfo(str, PackageManager.ApplicationInfoFlags.of(0L));
                        } catch (PackageManager.NameNotFoundException unused) {
                            applicationInfo = null;
                        }
                        CharSequence applicationLabel = applicationInfo != null ? packageManagerForUser.getApplicationLabel(applicationInfo) : null;
                        StatusBarManagerInternal statusBarManagerInternal = this.mStatusBarManagerInternal;
                        if (statusBarManagerInternal != null) {
                            statusBarManagerInternal.setIcon(this.mSlotIme, str, i, 0, applicationLabel != null ? applicationLabel.toString() : null);
                            this.mStatusBarManagerInternal.setIconVisibility(this.mSlotIme, true);
                        }
                    }
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                } catch (Throwable th) {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    throw th;
                }
            }
        }
    }

    @GuardedBy({"ImfLock.class"})
    private void hideStatusBarIconLocked() {
        StatusBarManagerInternal statusBarManagerInternal = this.mStatusBarManagerInternal;
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.setIconVisibility(this.mSlotIme, false);
        }
    }

    @GuardedBy({"ImfLock.class"})
    private int getInputMethodNavButtonFlagsLocked() {
        Future<?> future = this.mImeDrawsImeNavBarResLazyInitFuture;
        if (future != null) {
            ConcurrentUtils.waitForFutureNoInterrupt(future, "Waiting for the lazy init of mImeDrawsImeNavBarRes");
        }
        OverlayableSystemBooleanResourceWrapper overlayableSystemBooleanResourceWrapper = this.mImeDrawsImeNavBarRes;
        return ((overlayableSystemBooleanResourceWrapper == null || !overlayableSystemBooleanResourceWrapper.get()) ? 0 : 1) | (shouldShowImeSwitcherLocked(3) ? 2 : 0);
    }

    @GuardedBy({"ImfLock.class"})
    private boolean shouldShowImeSwitcherLocked(int i) {
        if (this.mImmsWrapper.getExtImpl().shouldHideImeSwitcher() || !this.mShowOngoingImeSwitcherForPhones || this.mMenuController.getSwitchingDialogLocked() != null) {
            return false;
        }
        if ((this.mWindowManagerInternal.isKeyguardShowingAndNotOccluded() && this.mWindowManagerInternal.isKeyguardSecure(this.mSettings.getCurrentUserId())) || (i & 1) == 0 || (i & 4) != 0) {
            return false;
        }
        if (this.mWindowManagerInternal.isHardKeyboardAvailable()) {
            return true;
        }
        if ((i & 2) == 0) {
            return false;
        }
        ArrayList<InputMethodInfo> enabledInputMethodListWithFilterLocked = this.mSettings.getEnabledInputMethodListWithFilterLocked(new Predicate() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((InputMethodInfo) obj).shouldShowInInputMethodPicker();
            }
        });
        int size = enabledInputMethodListWithFilterLocked.size();
        if (size > 2) {
            return true;
        }
        if (size < 1) {
            return false;
        }
        InputMethodSubtype inputMethodSubtype = null;
        InputMethodSubtype inputMethodSubtype2 = null;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < size; i4++) {
            List<InputMethodSubtype> enabledInputMethodSubtypeListLocked = this.mSettings.getEnabledInputMethodSubtypeListLocked(enabledInputMethodListWithFilterLocked.get(i4), true);
            int size2 = enabledInputMethodSubtypeListLocked.size();
            if (size2 == 0) {
                i2++;
            } else {
                for (int i5 = 0; i5 < size2; i5++) {
                    InputMethodSubtype inputMethodSubtype3 = enabledInputMethodSubtypeListLocked.get(i5);
                    if (inputMethodSubtype3.isAuxiliary()) {
                        i3++;
                        inputMethodSubtype2 = inputMethodSubtype3;
                    } else {
                        i2++;
                        inputMethodSubtype = inputMethodSubtype3;
                    }
                }
            }
        }
        if (i2 > 1 || i3 > 1) {
            return true;
        }
        if (i2 == 1 && i3 == 1) {
            return inputMethodSubtype == null || inputMethodSubtype2 == null || !((inputMethodSubtype.getLocale().equals(inputMethodSubtype2.getLocale()) || inputMethodSubtype2.overridesImplicitlyEnabledSubtype() || inputMethodSubtype.overridesImplicitlyEnabledSubtype()) && inputMethodSubtype.containsExtraValueKey(TAG_TRY_SUPPRESSING_IME_SWITCHER));
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImeWindowStatus(IBinder iBinder, int i, int i2) {
        int topFocusedDisplayId = this.mWindowManagerInternal.getTopFocusedDisplayId();
        synchronized (ImfLock.class) {
            if (calledWithValidTokenLocked(iBinder)) {
                int i3 = this.mCurTokenDisplayId;
                if (i3 == topFocusedDisplayId || i3 == 0) {
                    if (this.mImmsWrapper.getExtImpl().onSetImeWindowStatus(i, i2)) {
                        return;
                    }
                    this.mImeWindowVis = i;
                    this.mBackDisposition = i2;
                    updateSystemUiLocked(i, i2);
                    boolean z = false;
                    if (i2 != 1 && (i2 == 2 || (i & 2) != 0)) {
                        z = true;
                    }
                    this.mWindowManagerInternal.setDismissImeOnBackKeyPressed(z);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportStartInput(IBinder iBinder, IBinder iBinder2) {
        synchronized (ImfLock.class) {
            if (calledWithValidTokenLocked(iBinder)) {
                IBinder iBinder3 = this.mImeTargetWindowMap.get(iBinder2);
                if (iBinder3 != null) {
                    this.mWindowManagerInternal.updateInputMethodTargetWindow(iBinder, iBinder3);
                }
                this.mLastImeTargetWindow = iBinder3;
            }
        }
    }

    private void updateImeWindowStatus(boolean z) {
        synchronized (ImfLock.class) {
            if (z) {
                updateSystemUiLocked(0, this.mBackDisposition);
            } else {
                updateSystemUiLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void updateSystemUiLocked() {
        updateSystemUiLocked(this.mImeWindowVis, this.mBackDisposition);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public void updateSystemUiLocked(int i, int i2) {
        if (getCurTokenLocked() == null) {
            return;
        }
        if (DEBUG) {
            Slog.d(this.TAG, "IME window vis: " + i + " active: " + (i & 1) + " inv: " + (i & 4) + " displayId: " + this.mCurTokenDisplayId);
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (this.mCurPerceptible) {
                i &= -9;
            } else if ((i & 2) != 0) {
                i = (i & (-3)) | 8;
            }
            int i3 = i;
            boolean shouldShowImeSwitcherLocked = shouldShowImeSwitcherLocked(i3);
            StatusBarManagerInternal statusBarManagerInternal = this.mStatusBarManagerInternal;
            if (statusBarManagerInternal != null) {
                statusBarManagerInternal.setImeWindowStatus(this.mCurTokenDisplayId, getCurTokenLocked(), i3, i2, shouldShowImeSwitcherLocked);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @GuardedBy({"ImfLock.class"})
    void updateFromSettingsLocked(boolean z) {
        updateInputMethodsFromSettingsLocked(z);
        this.mMenuController.updateKeyboardFromSettingsLocked();
    }

    @GuardedBy({"ImfLock.class"})
    void updateInputMethodsFromSettingsLocked(boolean z) {
        ApplicationInfo applicationInfo;
        if (z) {
            PackageManager packageManagerForUser = getPackageManagerForUser(this.mContext, this.mSettings.getCurrentUserId());
            ArrayList<InputMethodInfo> enabledInputMethodListLocked = this.mSettings.getEnabledInputMethodListLocked();
            for (int i = 0; i < enabledInputMethodListLocked.size(); i++) {
                InputMethodInfo inputMethodInfo = enabledInputMethodListLocked.get(i);
                try {
                    applicationInfo = packageManagerForUser.getApplicationInfo(inputMethodInfo.getPackageName(), PackageManager.ApplicationInfoFlags.of(32768L));
                } catch (PackageManager.NameNotFoundException unused) {
                    applicationInfo = null;
                }
                if (applicationInfo != null && applicationInfo.enabledSetting == 4) {
                    if (DEBUG) {
                        Slog.d(this.TAG, "Update state(" + inputMethodInfo.getId() + "): DISABLED_UNTIL_USED -> DEFAULT");
                    }
                    packageManagerForUser.setApplicationEnabledSetting(inputMethodInfo.getPackageName(), 0, 1);
                }
            }
        }
        String selectedInputMethod = this.mSettings.getSelectedInputMethod();
        if (TextUtils.isEmpty(selectedInputMethod) && chooseNewDefaultIMELocked()) {
            selectedInputMethod = this.mSettings.getSelectedInputMethod();
        }
        if (!TextUtils.isEmpty(selectedInputMethod)) {
            try {
                setInputMethodLocked(selectedInputMethod, this.mSettings.getSelectedInputMethodSubtypeId(selectedInputMethod));
            } catch (IllegalArgumentException e) {
                Slog.w(this.TAG, "Unknown input method from prefs: " + selectedInputMethod, e);
                resetCurrentMethodAndClientLocked(5);
            }
        } else {
            resetCurrentMethodAndClientLocked(4);
        }
        this.mSwitchingController.resetCircularListLocked(this.mContext);
        this.mHardwareKeyboardShortcutController.reset(this.mSettings);
        sendOnNavButtonFlagsChangedLocked();
    }

    @GuardedBy({"ImfLock.class"})
    private void notifyInputMethodSubtypeChangedLocked(int i, InputMethodInfo inputMethodInfo, InputMethodSubtype inputMethodSubtype) {
        if (inputMethodSubtype == null || !inputMethodSubtype.isSuitableForPhysicalKeyboardLayoutMapping()) {
            inputMethodSubtype = null;
        }
        this.mInputManagerInternal.onInputMethodSubtypeChangedForKeyboardLayoutMapping(i, inputMethodSubtype != null ? InputMethodSubtypeHandle.of(inputMethodInfo, inputMethodSubtype) : null, inputMethodSubtype);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void setInputMethodLocked(String str, int i) {
        InputMethodSubtype currentInputMethodSubtypeLocked;
        InputMethodInfo inputMethodInfo = this.mMethodMap.get(str);
        if (inputMethodInfo == null) {
            throw getExceptionForUnknownImeId(str);
        }
        if (str.equals(getSelectedMethodIdLocked())) {
            int currentUserId = this.mSettings.getCurrentUserId();
            int subtypeCount = inputMethodInfo.getSubtypeCount();
            if (subtypeCount <= 0) {
                notifyInputMethodSubtypeChangedLocked(currentUserId, inputMethodInfo, null);
                return;
            }
            InputMethodSubtype inputMethodSubtype = this.mCurrentSubtype;
            if (i >= 0 && i < subtypeCount) {
                currentInputMethodSubtypeLocked = inputMethodInfo.getSubtypeAt(i);
            } else {
                currentInputMethodSubtypeLocked = getCurrentInputMethodSubtypeLocked();
            }
            if (currentInputMethodSubtypeLocked == null || inputMethodSubtype == null) {
                Slog.w(this.TAG, "Illegal subtype state: old subtype = " + inputMethodSubtype + ", new subtype = " + currentInputMethodSubtypeLocked);
                notifyInputMethodSubtypeChangedLocked(currentUserId, inputMethodInfo, null);
                return;
            }
            if (currentInputMethodSubtypeLocked.equals(inputMethodSubtype)) {
                return;
            }
            setSelectedInputMethodAndSubtypeLocked(inputMethodInfo, i, true);
            IInputMethodInvoker curMethodLocked = getCurMethodLocked();
            if (curMethodLocked != null) {
                updateSystemUiLocked(this.mImeWindowVis, this.mBackDisposition);
                curMethodLocked.changeInputMethodSubtype(currentInputMethodSubtypeLocked);
                return;
            }
            return;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            setSelectedInputMethodAndSubtypeLocked(inputMethodInfo, i, false);
            setSelectedMethodIdLocked(str);
            this.mImmsWrapper.getExtImpl().unfreezeInputMethodPackage(inputMethodInfo);
            if (this.mActivityManagerInternal.isSystemReady()) {
                Intent intent = new Intent("android.intent.action.INPUT_METHOD_CHANGED");
                intent.addFlags(536870912);
                intent.putExtra("input_method_id", str);
                this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
            }
            unbindCurrentClientLocked(2);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean showSoftInput(IInputMethodClient iInputMethodClient, IBinder iBinder, ImeTracker.Token token, int i, int i2, ResultReceiver resultReceiver, int i3) {
        Trace.traceBegin(32L, "IMMS.showSoftInput");
        int callingUid = Binder.getCallingUid();
        ImeTracing.getInstance().triggerManagerServiceDump("InputMethodManagerService#showSoftInput");
        synchronized (ImfLock.class) {
            if (!canInteractWithImeLocked(callingUid, iInputMethodClient, "showSoftInput", token)) {
                ImeTracker.forLogging().onFailed(token, 3);
                Trace.traceEnd(32L);
                return false;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                if (DEBUG) {
                    Slog.v(this.TAG, "Client requesting input be shown");
                }
                return showCurrentInputLocked(iBinder, token, i, i2, resultReceiver, i3);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                Trace.traceEnd(32L);
            }
        }
    }

    public void startStylusHandwriting(IInputMethodClient iInputMethodClient) {
        Trace.traceBegin(32L, "IMMS.startStylusHandwriting");
        try {
            ImeTracing.getInstance().triggerManagerServiceDump("InputMethodManagerService#startStylusHandwriting");
            int callingUid = Binder.getCallingUid();
            synchronized (ImfLock.class) {
                this.mHwController.clearPendingHandwritingDelegation();
                if (canInteractWithImeLocked(callingUid, iInputMethodClient, "startStylusHandwriting", null)) {
                    if (!hasSupportedStylusLocked()) {
                        Slog.w(this.TAG, "No supported Stylus hardware found on device. Ignoring startStylusHandwriting()");
                        return;
                    }
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        if (!this.mBindingController.supportsStylusHandwriting()) {
                            Slog.w(this.TAG, "Stylus HW unsupported by IME. Ignoring startStylusHandwriting()");
                            return;
                        }
                        OptionalInt currentRequestId = this.mHwController.getCurrentRequestId();
                        if (!currentRequestId.isPresent()) {
                            Slog.e(this.TAG, "Stylus handwriting was not initialized.");
                            return;
                        }
                        if (!this.mHwController.isStylusGestureOngoing()) {
                            Slog.e(this.TAG, "There is no ongoing stylus gesture to start stylus handwriting.");
                            return;
                        }
                        if (this.mHwController.hasOngoingStylusHandwritingSession()) {
                            Slog.e(this.TAG, "Stylus handwriting session is already ongoing. Ignoring startStylusHandwriting().");
                            return;
                        }
                        if (DEBUG) {
                            Slog.v(this.TAG, "Client requesting Stylus Handwriting to be started");
                        }
                        IInputMethodInvoker curMethodLocked = getCurMethodLocked();
                        if (curMethodLocked != null) {
                            curMethodLocked.canStartStylusHandwriting(currentRequestId.getAsInt());
                        }
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            }
        } finally {
            Trace.traceEnd(32L);
        }
    }

    public void prepareStylusHandwritingDelegation(IInputMethodClient iInputMethodClient, int i, String str, String str2) {
        if (!isStylusHandwritingEnabled(this.mContext, i)) {
            Slog.w(this.TAG, "Can not prepare stylus handwriting delegation. Stylus handwriting pref is disabled for user: " + i);
            return;
        }
        if (!verifyClientAndPackageMatch(iInputMethodClient, str2)) {
            Slog.w(this.TAG, "prepareStylusHandwritingDelegation() fail");
            throw new IllegalArgumentException("Delegator doesn't match Uid");
        }
        schedulePrepareStylusHandwritingDelegation(str, str2);
    }

    public boolean acceptStylusHandwritingDelegation(IInputMethodClient iInputMethodClient, int i, String str, String str2) {
        if (!isStylusHandwritingEnabled(this.mContext, i)) {
            Slog.w(this.TAG, "Can not accept stylus handwriting delegation. Stylus handwriting pref is disabled for user: " + i);
            return false;
        }
        if (!verifyDelegator(iInputMethodClient, str, str2)) {
            return false;
        }
        startStylusHandwriting(iInputMethodClient);
        return true;
    }

    private boolean verifyClientAndPackageMatch(IInputMethodClient iInputMethodClient, String str) {
        ClientState clientState;
        synchronized (ImfLock.class) {
            clientState = this.mClients.get(iInputMethodClient.asBinder());
        }
        if (clientState == null) {
            throw new IllegalArgumentException("unknown client " + iInputMethodClient.asBinder());
        }
        return InputMethodUtils.checkIfPackageBelongsToUid(this.mPackageManagerInternal, clientState.mUid, str);
    }

    private boolean verifyDelegator(IInputMethodClient iInputMethodClient, String str, String str2) {
        if (!verifyClientAndPackageMatch(iInputMethodClient, str)) {
            Slog.w(this.TAG, "Delegate package does not belong to the same user. Ignoring startStylusHandwriting");
            return false;
        }
        synchronized (ImfLock.class) {
            if (!str2.equals(this.mHwController.getDelegatorPackageName())) {
                Slog.w(this.TAG, "Delegator package does not match. Ignoring startStylusHandwriting");
                return false;
            }
            if (str.equals(this.mHwController.getDelegatePackageName())) {
                return true;
            }
            Slog.w(this.TAG, "Delegate package does not match. Ignoring startStylusHandwriting");
            return false;
        }
    }

    public void reportPerceptibleAsync(final IBinder iBinder, final boolean z) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda11
            public final void runOrThrow() {
                InputMethodManagerService.this.lambda$reportPerceptibleAsync$5(iBinder, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportPerceptibleAsync$5(IBinder iBinder, boolean z) throws Exception {
        Objects.requireNonNull(iBinder, "windowToken must not be null");
        synchronized (ImfLock.class) {
            if (this.mCurFocusedWindow == iBinder && this.mCurPerceptible != z) {
                this.mCurPerceptible = z;
                updateSystemUiLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public boolean showCurrentInputLocked(IBinder iBinder, ImeTracker.Token token, int i, ResultReceiver resultReceiver, int i2) {
        return showCurrentInputLocked(iBinder, token, i, 0, resultReceiver, i2);
    }

    @GuardedBy({"ImfLock.class"})
    boolean showCurrentInputLocked(IBinder iBinder, ImeTracker.Token token, int i, int i2, ResultReceiver resultReceiver, int i3) {
        if (token == null) {
            token = createStatsTokenForFocusedClient(true, 3, i3);
        }
        ImeTracker.Token token2 = token;
        if (this.mImmsWrapper.getExtImpl().shouldInterceptIme(this.mCurTokenDisplayId) || !this.mVisibilityStateComputer.onImeShowFlags(token2, i)) {
            return false;
        }
        if (!this.mSystemReady) {
            ImeTracker.forLogging().onFailed(token2, 5);
            return false;
        }
        ImeTracker.forLogging().onProgress(token2, 5);
        if (this.mImmsWrapper.getExtImpl().shouldIgnoreShowBySynergy(getSelectedMethodIdLocked())) {
            return false;
        }
        this.mVisibilityStateComputer.requestImeVisibility(iBinder, true);
        this.mBindingController.setCurrentMethodVisible();
        IInputMethodInvoker curMethodLocked = getCurMethodLocked();
        ImeTracker.forLogging().onCancelled(this.mCurStatsToken, 8);
        if (curMethodLocked != null) {
            ImeTracker.forLogging().onProgress(token2, 9);
            this.mImmsWrapper.getExtImpl().updateOsenseAction();
            this.mCurStatsToken = null;
            if (i2 != 0) {
                curMethodLocked.updateEditorToolType(i2);
            }
            this.mVisibilityApplier.performShowIme(iBinder, token2, this.mVisibilityStateComputer.getImeShowFlags(), resultReceiver, i3);
            this.mVisibilityStateComputer.setInputShown(true);
            return true;
        }
        ImeTracker.forLogging().onProgress(token2, 8);
        this.mCurStatsToken = token2;
        return false;
    }

    public boolean hideSoftInput(IInputMethodClient iInputMethodClient, IBinder iBinder, ImeTracker.Token token, int i, ResultReceiver resultReceiver, int i2) {
        int callingUid = Binder.getCallingUid();
        ImeTracing.getInstance().triggerManagerServiceDump("InputMethodManagerService#hideSoftInput");
        synchronized (ImfLock.class) {
            if (!canInteractWithImeLocked(callingUid, iInputMethodClient, "hideSoftInput", token)) {
                if (isInputShown()) {
                    ImeTracker.forLogging().onFailed(token, 3);
                } else {
                    ImeTracker.forLogging().onCancelled(token, 3);
                }
                return false;
            }
            ClientState clientState = this.mClients.get(iInputMethodClient.asBinder());
            if (clientState.mSelfReportedDisplayId != this.mCurTokenDisplayId) {
                if (DEBUG) {
                    Slog.w(this.TAG, "Ignoring hideSoftInput of displayId " + clientState.mSelfReportedDisplayId + SliceClientPermissions.SliceAuthority.DELIMITER + this.mCurTokenDisplayId);
                }
                return false;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                Trace.traceBegin(32L, "IMMS.hideSoftInput");
                if (DEBUG) {
                    Slog.v(this.TAG, "Client requesting input be hidden");
                }
                return hideCurrentInputLocked(iBinder, token, i, resultReceiver, i2);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                Trace.traceEnd(32L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public boolean hideCurrentInputLocked(IBinder iBinder, ImeTracker.Token token, int i, ResultReceiver resultReceiver, int i2) {
        if (token == null) {
            token = createStatsTokenForFocusedClient(false, 4, i2);
        }
        ImeTracker.Token token2 = token;
        if (!this.mVisibilityStateComputer.canHideIme(token2, i)) {
            return false;
        }
        boolean z = getCurMethodLocked() != null && (isInputShown() || (this.mImeWindowVis & 1) != 0 || this.mImmsWrapper.getExtImpl().hideInputMethodForce(i, i2));
        this.mVisibilityStateComputer.requestImeVisibility(iBinder, false);
        if (z) {
            ImeTracker.forLogging().onProgress(token2, 10);
            this.mVisibilityApplier.performHideIme(iBinder, token2, i, resultReceiver, i2);
        } else {
            ImeTracker.forLogging().onCancelled(token2, 10);
        }
        this.mBindingController.setCurrentMethodNotVisible();
        this.mVisibilityStateComputer.clearImeShowFlags();
        ImeTracker.forLogging().onCancelled(this.mCurStatsToken, 8);
        this.mCurStatsToken = null;
        return z;
    }

    private boolean isImeClientFocused(IBinder iBinder, ClientState clientState) {
        return this.mWindowManagerInternal.hasInputMethodClientFocus(iBinder, clientState.mUid, clientState.mPid, clientState.mSelfReportedDisplayId) == 0;
    }

    public InputBindResult startInputOrWindowGainedFocus(int i, IInputMethodClient iInputMethodClient, IBinder iBinder, int i2, int i3, int i4, EditorInfo editorInfo, IRemoteInputConnection iRemoteInputConnection, IRemoteAccessibilityInputConnection iRemoteAccessibilityInputConnection, int i5, int i6, ImeOnBackInvokedDispatcher imeOnBackInvokedDispatcher) {
        InputBindResult startInputOrWindowGainedFocusInternalLocked;
        UserHandle userHandle;
        if (UserHandle.getCallingUserId() != i6) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
            if (editorInfo == null || (userHandle = editorInfo.targetInputMethodUser) == null || userHandle.getIdentifier() != i6) {
                throw new InvalidParameterException("EditorInfo#targetInputMethodUser must also be specified for cross-user startInputOrWindowGainedFocus()");
            }
        }
        if (iBinder == null) {
            Slog.e(this.TAG, "windowToken cannot be null.");
            return InputBindResult.NULL;
        }
        try {
            Trace.traceBegin(32L, "IMMS.startInputOrWindowGainedFocus");
            ImeTracing.getInstance().triggerManagerServiceDump("InputMethodManagerService#startInputOrWindowGainedFocus");
            synchronized (ImfLock.class) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    startInputOrWindowGainedFocusInternalLocked = startInputOrWindowGainedFocusInternalLocked(i, iInputMethodClient, iBinder, i2, i3, i4, editorInfo, iRemoteInputConnection, iRemoteAccessibilityInputConnection, i5, i6, imeOnBackInvokedDispatcher);
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                } catch (Throwable th) {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    throw th;
                }
            }
            if (startInputOrWindowGainedFocusInternalLocked != null) {
                return startInputOrWindowGainedFocusInternalLocked;
            }
            Slog.wtf(this.TAG, "InputBindResult is @NonNull. startInputReason=" + InputMethodDebug.startInputReasonToString(i) + " windowFlags=#" + Integer.toHexString(i4) + " editorInfo=" + editorInfo);
            return InputBindResult.NULL;
        } finally {
            Trace.traceEnd(32L);
        }
    }

    @GuardedBy({"ImfLock.class"})
    InputBindResult startInputOrWindowGainedFocusInternalLocked(int i, IInputMethodClient iInputMethodClient, IBinder iBinder, int i2, int i3, int i4, EditorInfo editorInfo, IRemoteInputConnection iRemoteInputConnection, IRemoteAccessibilityInputConnection iRemoteAccessibilityInputConnection, int i5, int i6, ImeOnBackInvokedDispatcher imeOnBackInvokedDispatcher) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        ImeTracker.Token token;
        if (DEBUG) {
            Slog.v(this.TAG, "startInputOrWindowGainedFocusInternalLocked: reason=" + InputMethodDebug.startInputReasonToString(i) + " client=" + iInputMethodClient.asBinder() + " inputContext=" + iRemoteInputConnection + " editorInfo=" + editorInfo + " startInputFlags=" + InputMethodDebug.startInputFlagsToString(i2) + " softInputMode=" + InputMethodDebug.softInputModeToString(i3) + " windowFlags=#" + Integer.toHexString(i4) + " unverifiedTargetSdkVersion=" + i5 + " userId=" + i6 + " imeDispatcher=" + imeOnBackInvokedDispatcher);
        }
        if (!this.mUserManagerInternal.isUserRunning(i6)) {
            Slog.w(this.TAG, "User #" + i6 + " is not running.");
            return InputBindResult.INVALID_USER;
        }
        ClientState clientState = this.mClients.get(iInputMethodClient.asBinder());
        if (clientState == null) {
            throw new IllegalArgumentException("unknown client " + iInputMethodClient.asBinder());
        }
        int hasInputMethodClientFocus = this.mWindowManagerInternal.hasInputMethodClientFocus(iBinder, clientState.mUid, clientState.mPid, clientState.mSelfReportedDisplayId);
        if (hasInputMethodClientFocus != -3) {
            if (hasInputMethodClientFocus == -2) {
                Slog.e(this.TAG, "startInputOrWindowGainedFocusInternal: display ID mismatch.");
                return InputBindResult.DISPLAY_ID_MISMATCH;
            }
            if (hasInputMethodClientFocus == -1) {
                if (DEBUG) {
                    Slog.w(this.TAG, "Focus gain on non-focused client " + clientState.mClient + " (uid=" + clientState.mUid + " pid=" + clientState.mPid + ")");
                }
                if (!this.mImmsWrapper.getExtImpl().shouldIgnoreFocusCheck(iBinder, hasInputMethodClientFocus)) {
                    return InputBindResult.NOT_IME_TARGET_WINDOW;
                }
            }
        } else if (!this.mImmsWrapper.getExtImpl().shouldIgnoreFocusCheck(iBinder, hasInputMethodClientFocus)) {
            return InputBindResult.INVALID_DISPLAY_ID;
        }
        UserSwitchHandlerTask userSwitchHandlerTask = this.mUserSwitchHandlerTask;
        if (userSwitchHandlerTask != null) {
            if (i6 == userSwitchHandlerTask.mToUserId) {
                scheduleSwitchUserTaskLocked(i6, clientState.mClient);
                return InputBindResult.USER_SWITCHING;
            }
            for (int i7 : this.mUserManagerInternal.getProfileIds(this.mSettings.getCurrentUserId(), false)) {
                if (i7 == i6) {
                    scheduleSwitchUserTaskLocked(i6, clientState.mClient);
                    return InputBindResult.USER_SWITCHING;
                }
            }
            return InputBindResult.INVALID_USER;
        }
        boolean shouldClearShowForcedFlag = this.mImePlatformCompatUtils.shouldClearShowForcedFlag(clientState.mUid);
        ImeVisibilityStateComputer imeVisibilityStateComputer = this.mVisibilityStateComputer;
        boolean z5 = imeVisibilityStateComputer.mShowForced;
        if (this.mCurFocusedWindow != iBinder && z5 && shouldClearShowForcedFlag) {
            imeVisibilityStateComputer.mShowForced = false;
        }
        if (!this.mSettings.isCurrentProfile(i6)) {
            Slog.w(this.TAG, "A background user is requesting window. Hiding IME.");
            Slog.w(this.TAG, "If you need to impersonate a foreground user/profile from a background user, use EditorInfo.targetInputMethodUser with INTERACT_ACROSS_USERS_FULL permission.");
            hideCurrentInputLocked(this.mCurFocusedWindow, null, 0, null, 11);
            return InputBindResult.INVALID_USER;
        }
        if (i6 != this.mSettings.getCurrentUserId() && !this.mImmsWrapper.getExtImpl().isMultiAppUserId(i6)) {
            scheduleSwitchUserTaskLocked(i6, clientState.mClient);
            return InputBindResult.USER_SWITCHING;
        }
        boolean z6 = this.mCurFocusedWindow == iBinder;
        boolean z7 = (i2 & 2) != 0;
        boolean z8 = (i2 & 8) != 0;
        int initialToolType = editorInfo != null ? editorInfo.getInitialToolType() : 0;
        if (z6) {
            z = z7;
            z2 = true;
            z3 = false;
        } else {
            z = z7;
            z2 = true;
            z3 = false;
            if (this.mImmsWrapper.getExtImpl().shouldIgnoreStartInput(this.mContext, i2, editorInfo, clientState.mSelfReportedDisplayId, this.mCurTokenDisplayId, isInputShown())) {
                return InputBindResult.INVALID_DISPLAY_ID;
            }
        }
        ImeVisibilityStateComputer.ImeTargetWindowState imeTargetWindowState = new ImeVisibilityStateComputer.ImeTargetWindowState(i3, i4, !z6, z, z8, initialToolType);
        this.mVisibilityStateComputer.setWindowState(iBinder, imeTargetWindowState);
        boolean z9 = z;
        this.mImmsWrapper.getExtImpl().notifyImeAttributeChanged(z9, editorInfo, clientState.mSelfReportedDisplayId);
        if (z6 && z9) {
            if (DEBUG) {
                Slog.w(this.TAG, "Window already focused, ignoring focus gain of: " + iInputMethodClient + " editorInfo=" + editorInfo + ", token = " + iBinder + ", startInputReason=" + InputMethodDebug.startInputReasonToString(i));
            }
            if (editorInfo != null) {
                return startInputUncheckedLocked(clientState, iRemoteInputConnection, iRemoteAccessibilityInputConnection, editorInfo, i2, i, i5, imeOnBackInvokedDispatcher);
            }
            return new InputBindResult(4, (IInputMethodSession) null, (SparseArray) null, (InputChannel) null, (String) null, -1, (Matrix) null, false);
        }
        this.mCurFocusedWindow = iBinder;
        this.mCurFocusedWindowSoftInputMode = i3;
        this.mCurFocusedWindowClient = clientState;
        this.mCurFocusedWindowEditorInfo = editorInfo;
        this.mCurPerceptible = z2;
        ImeVisibilityStateComputer.ImeVisibilityResult computeState = this.mVisibilityStateComputer.computeState(imeTargetWindowState, InputMethodUtils.isSoftInputModeStateVisibleAllowed(i5, i2));
        InputBindResult inputBindResult = null;
        if (computeState != null) {
            int reason = computeState.getReason();
            if ((reason == 6 || reason == 7 || reason == 8 || reason == 23) && editorInfo != null) {
                token = null;
                inputBindResult = startInputUncheckedLocked(clientState, iRemoteInputConnection, iRemoteAccessibilityInputConnection, editorInfo, i2, i, i5, imeOnBackInvokedDispatcher);
                z4 = true;
            } else {
                token = null;
                inputBindResult = null;
                z4 = z3;
            }
            this.mVisibilityApplier.applyImeVisibility(this.mCurFocusedWindow, token, computeState.getState(), computeState.getReason());
            if (computeState.getReason() == 12 && clientState.mSelfReportedDisplayId != this.mCurTokenDisplayId) {
                this.mBindingController.unbindCurrentMethod();
            }
        } else {
            z4 = z3;
        }
        if (z4) {
            return inputBindResult;
        }
        if (editorInfo != null) {
            return startInputUncheckedLocked(clientState, iRemoteInputConnection, iRemoteAccessibilityInputConnection, editorInfo, i2, i, i5, imeOnBackInvokedDispatcher);
        }
        return InputBindResult.NULL_EDITOR_INFO;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public void showCurrentInputImplicitLocked(IBinder iBinder, int i) {
        showCurrentInputLocked(iBinder, null, 1, null, i);
    }

    @GuardedBy({"ImfLock.class"})
    private boolean canInteractWithImeLocked(int i, IInputMethodClient iInputMethodClient, String str, ImeTracker.Token token) {
        ClientState clientState = this.mCurClient;
        if (clientState == null || iInputMethodClient == null || clientState.mClient.asBinder() != iInputMethodClient.asBinder()) {
            ClientState clientState2 = this.mClients.get(iInputMethodClient.asBinder());
            if (clientState2 == null) {
                ImeTracker.forLogging().onFailed(token, 2);
                throw new IllegalArgumentException("unknown client " + iInputMethodClient.asBinder());
            }
            ImeTracker.forLogging().onProgress(token, 2);
            if (!isImeClientFocused(this.mCurFocusedWindow, clientState2)) {
                Slog.w(this.TAG, String.format("Ignoring %s of uid %d : %s", str, Integer.valueOf(i), iInputMethodClient));
                return false;
            }
        }
        ImeTracker.forLogging().onProgress(token, 3);
        return true;
    }

    @GuardedBy({"ImfLock.class"})
    private boolean canShowInputMethodPickerLocked(IInputMethodClient iInputMethodClient) {
        int callingUid = Binder.getCallingUid();
        ClientState clientState = this.mCurFocusedWindowClient;
        if (clientState == null || iInputMethodClient == null || clientState.mClient.asBinder() != iInputMethodClient.asBinder()) {
            return this.mSettings.getCurrentUserId() == UserHandle.getUserId(callingUid) && getCurIntentLocked() != null && InputMethodUtils.checkIfPackageBelongsToUid(this.mPackageManagerInternal, callingUid, getCurIntentLocked().getComponent().getPackageName());
        }
        return true;
    }

    public void showInputMethodPickerFromClient(IInputMethodClient iInputMethodClient, int i) {
        synchronized (ImfLock.class) {
            if (!canShowInputMethodPickerLocked(iInputMethodClient)) {
                Slog.w(this.TAG, "Ignoring showInputMethodPickerFromClient of uid " + Binder.getCallingUid() + ": " + iInputMethodClient);
                return;
            }
            ClientState clientState = this.mCurClient;
            this.mHandler.obtainMessage(1, i, clientState != null ? clientState.mSelfReportedDisplayId : 0).sendToTarget();
        }
    }

    @EnforcePermission("android.permission.WRITE_SECURE_SETTINGS")
    public void showInputMethodPickerFromSystem(int i, int i2) {
        super.showInputMethodPickerFromSystem_enforcePermission();
        this.mHandler.obtainMessage(1, i, i2).sendToTarget();
    }

    @EnforcePermission("android.permission.TEST_INPUT_METHOD")
    public boolean isInputMethodPickerShownForTest() {
        boolean isisInputMethodPickerShownForTestLocked;
        super.isInputMethodPickerShownForTest_enforcePermission();
        synchronized (ImfLock.class) {
            isisInputMethodPickerShownForTestLocked = this.mMenuController.isisInputMethodPickerShownForTestLocked();
        }
        return isisInputMethodPickerShownForTestLocked;
    }

    private static IllegalArgumentException getExceptionForUnknownImeId(String str) {
        return new IllegalArgumentException("Unknown id: " + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setInputMethod(IBinder iBinder, String str) {
        int callingUid = Binder.getCallingUid();
        int userId = UserHandle.getUserId(callingUid);
        synchronized (ImfLock.class) {
            if (calledWithValidTokenLocked(iBinder)) {
                InputMethodInfo inputMethodInfo = this.mMethodMap.get(str);
                if (inputMethodInfo == null || !canCallerAccessInputMethod(inputMethodInfo.getPackageName(), callingUid, userId, this.mSettings)) {
                    throw getExceptionForUnknownImeId(str);
                }
                setInputMethodWithSubtypeIdLocked(iBinder, str, -1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setInputMethodAndSubtype(IBinder iBinder, String str, InputMethodSubtype inputMethodSubtype) {
        int callingUid = Binder.getCallingUid();
        int userId = UserHandle.getUserId(callingUid);
        synchronized (ImfLock.class) {
            if (calledWithValidTokenLocked(iBinder)) {
                InputMethodInfo inputMethodInfo = this.mMethodMap.get(str);
                if (inputMethodInfo == null || !canCallerAccessInputMethod(inputMethodInfo.getPackageName(), callingUid, userId, this.mSettings)) {
                    throw getExceptionForUnknownImeId(str);
                }
                if (inputMethodSubtype != null) {
                    setInputMethodWithSubtypeIdLocked(iBinder, str, SubtypeUtils.getSubtypeIdFromHashCode(inputMethodInfo, inputMethodSubtype.hashCode()));
                } else {
                    setInputMethod(iBinder, str);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean switchToPreviousInputMethod(IBinder iBinder) {
        ArrayList<InputMethodInfo> enabledInputMethodListLocked;
        String locale;
        InputMethodSubtype findLastResortApplicableSubtypeLocked;
        synchronized (ImfLock.class) {
            if (!calledWithValidTokenLocked(iBinder)) {
                return false;
            }
            Pair<String, String> lastInputMethodAndSubtypeLocked = this.mSettings.getLastInputMethodAndSubtypeLocked();
            String str = null;
            InputMethodInfo inputMethodInfo = lastInputMethodAndSubtypeLocked != null ? this.mMethodMap.get(lastInputMethodAndSubtypeLocked.first) : null;
            int i = -1;
            if (lastInputMethodAndSubtypeLocked != null && inputMethodInfo != null) {
                boolean equals = inputMethodInfo.getId().equals(getSelectedMethodIdLocked());
                int parseInt = Integer.parseInt((String) lastInputMethodAndSubtypeLocked.second);
                InputMethodSubtype inputMethodSubtype = this.mCurrentSubtype;
                int hashCode = inputMethodSubtype == null ? -1 : inputMethodSubtype.hashCode();
                if (!equals || parseInt != hashCode) {
                    str = (String) lastInputMethodAndSubtypeLocked.first;
                    i = SubtypeUtils.getSubtypeIdFromHashCode(inputMethodInfo, parseInt);
                }
            }
            if (TextUtils.isEmpty(str) && !InputMethodUtils.canAddToLastInputMethod(this.mCurrentSubtype) && (enabledInputMethodListLocked = this.mSettings.getEnabledInputMethodListLocked()) != null) {
                int size = enabledInputMethodListLocked.size();
                InputMethodSubtype inputMethodSubtype2 = this.mCurrentSubtype;
                if (inputMethodSubtype2 == null) {
                    locale = this.mRes.getConfiguration().locale.toString();
                } else {
                    locale = inputMethodSubtype2.getLocale();
                }
                for (int i2 = 0; i2 < size; i2++) {
                    InputMethodInfo inputMethodInfo2 = enabledInputMethodListLocked.get(i2);
                    if (inputMethodInfo2.getSubtypeCount() > 0 && inputMethodInfo2.isSystem() && (findLastResortApplicableSubtypeLocked = SubtypeUtils.findLastResortApplicableSubtypeLocked(this.mRes, SubtypeUtils.getSubtypes(inputMethodInfo2), "keyboard", locale, true)) != null) {
                        str = inputMethodInfo2.getId();
                        i = SubtypeUtils.getSubtypeIdFromHashCode(inputMethodInfo2, findLastResortApplicableSubtypeLocked.hashCode());
                        if (findLastResortApplicableSubtypeLocked.getLocale().equals(locale)) {
                            break;
                        }
                    }
                }
            }
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            if (DEBUG) {
                Slog.d(this.TAG, "Switch to: " + inputMethodInfo.getId() + ", " + ((String) lastInputMethodAndSubtypeLocked.second) + ", from: " + getSelectedMethodIdLocked() + ", " + i);
            }
            setInputMethodWithSubtypeIdLocked(iBinder, str, i);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean switchToNextInputMethod(IBinder iBinder, boolean z) {
        synchronized (ImfLock.class) {
            if (!calledWithValidTokenLocked(iBinder)) {
                return false;
            }
            return switchToNextInputMethodLocked(iBinder, z);
        }
    }

    @GuardedBy({"ImfLock.class"})
    private boolean switchToNextInputMethodLocked(IBinder iBinder, boolean z) {
        InputMethodSubtypeSwitchingController.ImeSubtypeListItem nextInputMethodLocked = this.mSwitchingController.getNextInputMethodLocked(z, this.mMethodMap.get(getSelectedMethodIdLocked()), this.mCurrentSubtype);
        if (nextInputMethodLocked == null) {
            return false;
        }
        setInputMethodWithSubtypeIdLocked(iBinder, nextInputMethodLocked.mImi.getId(), nextInputMethodLocked.mSubtypeId);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldOfferSwitchingToNextInputMethod(IBinder iBinder) {
        synchronized (ImfLock.class) {
            if (calledWithValidTokenLocked(iBinder)) {
                return this.mSwitchingController.getNextInputMethodLocked(false, this.mMethodMap.get(getSelectedMethodIdLocked()), this.mCurrentSubtype) != null;
            }
            return false;
        }
    }

    public InputMethodSubtype getLastInputMethodSubtype(int i) {
        if (UserHandle.getCallingUserId() != i) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (ImfLock.class) {
            if (this.mSettings.getCurrentUserId() == i) {
                return this.mSettings.getLastInputMethodSubtypeLocked();
            }
            return new InputMethodUtils.InputMethodSettings(this.mContext, queryMethodMapForUser(i), i, false).getLastInputMethodSubtypeLocked();
        }
    }

    public void setAdditionalInputMethodSubtypes(String str, InputMethodSubtype[] inputMethodSubtypeArr, int i) {
        if (UserHandle.getCallingUserId() != i) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        int callingUid = Binder.getCallingUid();
        if (TextUtils.isEmpty(str) || inputMethodSubtypeArr == null) {
            return;
        }
        ArrayList<InputMethodSubtype> arrayList = new ArrayList<>();
        for (InputMethodSubtype inputMethodSubtype : inputMethodSubtypeArr) {
            if (!arrayList.contains(inputMethodSubtype)) {
                arrayList.add(inputMethodSubtype);
            } else {
                Slog.w(this.TAG, "Duplicated subtype definition found: " + inputMethodSubtype.getLocale() + ", " + inputMethodSubtype.getMode());
            }
        }
        synchronized (ImfLock.class) {
            if (this.mSystemReady) {
                if (this.mSettings.getCurrentUserId() == i) {
                    if (this.mSettings.setAdditionalInputMethodSubtypes(str, arrayList, this.mAdditionalSubtypeMap, this.mPackageManagerInternal, callingUid)) {
                        long clearCallingIdentity = Binder.clearCallingIdentity();
                        try {
                            buildInputMethodListLocked(false);
                            return;
                        } finally {
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                        }
                    }
                    return;
                }
                ArrayMap<String, InputMethodInfo> arrayMap = new ArrayMap<>();
                ArrayList<InputMethodInfo> arrayList2 = new ArrayList<>();
                ArrayMap<String, List<InputMethodSubtype>> arrayMap2 = new ArrayMap<>();
                AdditionalSubtypeUtils.load(arrayMap2, i);
                queryInputMethodServicesInternal(this.mContext, i, arrayMap2, arrayMap, arrayList2, 0, this.mSettings.getEnabledInputMethodNames());
                new InputMethodUtils.InputMethodSettings(this.mContext, arrayMap, i, false).setAdditionalInputMethodSubtypes(str, arrayList, arrayMap2, this.mPackageManagerInternal, callingUid);
            }
        }
    }

    public void setExplicitlyEnabledInputMethodSubtypes(String str, int[] iArr, int i) {
        InputMethodUtils.InputMethodSettings inputMethodSettings;
        if (UserHandle.getCallingUserId() != i) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        int callingUid = Binder.getCallingUid();
        ComponentName unflattenFromString = str != null ? ComponentName.unflattenFromString(str) : null;
        if (unflattenFromString == null || !InputMethodUtils.checkIfPackageBelongsToUid(this.mPackageManagerInternal, callingUid, unflattenFromString.getPackageName())) {
            throw new SecurityException("Calling UID=" + callingUid + " does not belong to imeId=" + str);
        }
        Objects.requireNonNull(iArr, "subtypeHashCodes must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (ImfLock.class) {
                boolean z = true;
                boolean z2 = this.mSettings.getCurrentUserId() == i;
                if (z2) {
                    inputMethodSettings = this.mSettings;
                } else {
                    Context context = this.mContext;
                    ArrayMap<String, InputMethodInfo> queryMethodMapForUser = queryMethodMapForUser(i);
                    if (this.mUserManagerInternal.isUserUnlocked(i)) {
                        z = false;
                    }
                    inputMethodSettings = new InputMethodUtils.InputMethodSettings(context, queryMethodMapForUser, i, z);
                }
                if (inputMethodSettings.setEnabledInputMethodSubtypes(str, iArr)) {
                    if (z2) {
                        SettingsObserver settingsObserver = this.mSettingsObserver;
                        if (settingsObserver != null) {
                            settingsObserver.mLastEnabled = inputMethodSettings.getEnabledInputMethodsStr();
                        }
                        updateInputMethodsFromSettingsLocked(false);
                    }
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Deprecated
    public int getInputMethodWindowVisibleHeight(final IInputMethodClient iInputMethodClient) {
        final int callingUid = Binder.getCallingUid();
        return ((Integer) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda6
            public final Object getOrThrow() {
                Integer lambda$getInputMethodWindowVisibleHeight$6;
                lambda$getInputMethodWindowVisibleHeight$6 = InputMethodManagerService.this.lambda$getInputMethodWindowVisibleHeight$6(callingUid, iInputMethodClient);
                return lambda$getInputMethodWindowVisibleHeight$6;
            }
        })).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$getInputMethodWindowVisibleHeight$6(int i, IInputMethodClient iInputMethodClient) throws Exception {
        synchronized (ImfLock.class) {
            if (!canInteractWithImeLocked(i, iInputMethodClient, "getInputMethodWindowVisibleHeight", null)) {
                if (!this.mLoggedDeniedGetInputMethodWindowVisibleHeightForUid.get(i)) {
                    EventLog.writeEvent(1397638484, "204906124", Integer.valueOf(i), "");
                    this.mLoggedDeniedGetInputMethodWindowVisibleHeightForUid.put(i, true);
                }
                return 0;
            }
            return Integer.valueOf(this.mWindowManagerInternal.getInputMethodWindowVisibleHeight(this.mCurTokenDisplayId));
        }
    }

    @EnforcePermission("android.permission.INTERNAL_SYSTEM_WINDOW")
    public void removeImeSurface() {
        super.removeImeSurface_enforcePermission();
        this.mHandler.obtainMessage(MSG_REMOVE_IME_SURFACE).sendToTarget();
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x0099, code lost:
    
        if (r6.mWindowManagerInternal.isUidAllowedOnDisplay(r8, r1.mUid) == false) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x009b, code lost:
    
        r2 = new com.android.server.inputmethod.InputMethodManagerService.VirtualDisplayInfo(r1, new android.graphics.Matrix());
        r6.mVirtualDisplayIdToParentMap.put(r8, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00c4, code lost:
    
        throw new java.lang.SecurityException(r1 + " cannot access to display #" + r8);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void reportVirtualDisplayGeometryAsync(IInputMethodClient iInputMethodClient, int i, float[] fArr) {
        IInputMethodClientInvoker create = IInputMethodClientInvoker.create(iInputMethodClient, this.mHandler);
        try {
            DisplayInfo displayInfo = this.mDisplayManagerInternal.getDisplayInfo(i);
            if (displayInfo == null) {
                throw new IllegalArgumentException("Cannot find display for non-existent displayId: " + i);
            }
            if (Binder.getCallingUid() != displayInfo.ownerUid) {
                throw new SecurityException("The caller doesn't own the display.");
            }
            synchronized (ImfLock.class) {
                ClientState clientState = this.mClients.get(create.asBinder());
                if (clientState == null) {
                    return;
                }
                if (fArr == null) {
                    VirtualDisplayInfo virtualDisplayInfo = this.mVirtualDisplayIdToParentMap.get(i);
                    if (virtualDisplayInfo == null) {
                        return;
                    }
                    if (virtualDisplayInfo.mParentClient != clientState) {
                        throw new SecurityException("Only the owner client can clear VirtualDisplayGeometry for display #" + i);
                    }
                    this.mVirtualDisplayIdToParentMap.remove(i);
                    return;
                }
                VirtualDisplayInfo virtualDisplayInfo2 = this.mVirtualDisplayIdToParentMap.get(i);
                if (virtualDisplayInfo2 != null && virtualDisplayInfo2.mParentClient != clientState) {
                    throw new InvalidParameterException("Display #" + i + " is already registered by " + virtualDisplayInfo2.mParentClient);
                }
                virtualDisplayInfo2.mMatrix.setValues(fArr);
                ClientState clientState2 = this.mCurClient;
                if (clientState2 != null && clientState2.mCurSession != null) {
                    int i2 = clientState2.mSelfReportedDisplayId;
                    Matrix matrix = null;
                    boolean z = false;
                    while (true) {
                        z |= i2 == i;
                        VirtualDisplayInfo virtualDisplayInfo3 = this.mVirtualDisplayIdToParentMap.get(i2);
                        if (virtualDisplayInfo3 == null) {
                            break;
                        }
                        if (matrix == null) {
                            matrix = new Matrix(virtualDisplayInfo3.mMatrix);
                        } else {
                            matrix.postConcat(virtualDisplayInfo3.mMatrix);
                        }
                        if (virtualDisplayInfo3.mParentClient.mSelfReportedDisplayId != this.mCurTokenDisplayId) {
                            i2 = virtualDisplayInfo2.mParentClient.mSelfReportedDisplayId;
                        } else if (z) {
                            float[] fArr2 = new float[9];
                            matrix.getValues(fArr2);
                            this.mCurClient.mClient.updateVirtualDisplayToScreenMatrix(getSequenceNumberLocked(), fArr2);
                        }
                    }
                }
            }
        } catch (Throwable th) {
            if (create != null) {
                create.throwExceptionFromSystem(th.toString());
            }
        }
    }

    public void removeImeSurfaceFromWindowAsync(IBinder iBinder) {
        this.mHandler.obtainMessage(MSG_REMOVE_IME_SURFACE_FROM_WINDOW, iBinder).sendToTarget();
    }

    private void registerDeviceListenerAndCheckStylusSupport() {
        final InputManager inputManager = (InputManager) this.mContext.getSystemService(InputManager.class);
        IntArray stylusInputDeviceIds = getStylusInputDeviceIds(inputManager);
        if (stylusInputDeviceIds.size() > 0) {
            synchronized (ImfLock.class) {
                IntArray intArray = new IntArray();
                this.mStylusIds = intArray;
                intArray.addAll(stylusInputDeviceIds);
            }
        }
        inputManager.registerInputDeviceListener(new InputManager.InputDeviceListener() { // from class: com.android.server.inputmethod.InputMethodManagerService.2
            @Override // android.hardware.input.InputManager.InputDeviceListener
            public void onInputDeviceAdded(int i) {
                InputDevice inputDevice = inputManager.getInputDevice(i);
                if (inputDevice == null || !InputMethodManagerService.isStylusDevice(inputDevice)) {
                    return;
                }
                add(i);
            }

            @Override // android.hardware.input.InputManager.InputDeviceListener
            public void onInputDeviceRemoved(int i) {
                remove(i);
            }

            @Override // android.hardware.input.InputManager.InputDeviceListener
            public void onInputDeviceChanged(int i) {
                InputDevice inputDevice = inputManager.getInputDevice(i);
                if (inputDevice == null) {
                    return;
                }
                if (InputMethodManagerService.isStylusDevice(inputDevice)) {
                    add(i);
                } else {
                    remove(i);
                }
            }

            private void add(int i) {
                synchronized (ImfLock.class) {
                    InputMethodManagerService.this.addStylusDeviceIdLocked(i);
                }
            }

            private void remove(int i) {
                synchronized (ImfLock.class) {
                    InputMethodManagerService.this.removeStylusDeviceIdLocked(i);
                }
            }
        }, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addStylusDeviceIdLocked(int i) {
        IntArray intArray = this.mStylusIds;
        if (intArray == null) {
            this.mStylusIds = new IntArray();
        } else if (intArray.indexOf(i) != -1) {
            return;
        }
        Slog.d(this.TAG, "New Stylus deviceId" + i + " added.");
        this.mStylusIds.add(i);
        if (this.mHwController.getCurrentRequestId().isPresent() || !this.mBindingController.supportsStylusHandwriting()) {
            return;
        }
        scheduleResetStylusHandwriting();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeStylusDeviceIdLocked(int i) {
        IntArray intArray = this.mStylusIds;
        if (intArray == null || intArray.size() == 0) {
            return;
        }
        int indexOf = this.mStylusIds.indexOf(i);
        if (indexOf != -1) {
            this.mStylusIds.remove(indexOf);
            Slog.d(this.TAG, "Stylus deviceId: " + i + " removed.");
        }
        if (this.mStylusIds.size() == 0) {
            this.mHwController.reset();
            scheduleRemoveStylusHandwritingWindow();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isStylusDevice(InputDevice inputDevice) {
        return inputDevice.supportsSource(16386) || inputDevice.supportsSource(49154);
    }

    @GuardedBy({"ImfLock.class"})
    private boolean hasSupportedStylusLocked() {
        IntArray intArray = this.mStylusIds;
        return (intArray == null || intArray.size() == 0) ? false : true;
    }

    @EnforcePermission("android.permission.TEST_INPUT_METHOD")
    public void addVirtualStylusIdForTestSession(IInputMethodClient iInputMethodClient) {
        super.addVirtualStylusIdForTestSession_enforcePermission();
        int callingUid = Binder.getCallingUid();
        synchronized (ImfLock.class) {
            if (canInteractWithImeLocked(callingUid, iInputMethodClient, "addVirtualStylusIdForTestSession", null)) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (DEBUG) {
                        Slog.v(this.TAG, "Adding virtual stylus id for session");
                    }
                    addStylusDeviceIdLocked(VIRTUAL_STYLUS_ID_FOR_TEST.intValue());
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }
    }

    @EnforcePermission("android.permission.TEST_INPUT_METHOD")
    public void setStylusWindowIdleTimeoutForTest(IInputMethodClient iInputMethodClient, long j) {
        super.setStylusWindowIdleTimeoutForTest_enforcePermission();
        int callingUid = Binder.getCallingUid();
        synchronized (ImfLock.class) {
            if (canInteractWithImeLocked(callingUid, iInputMethodClient, "setStylusWindowIdleTimeoutForTest", null)) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (DEBUG) {
                        Slog.v(this.TAG, "Setting stylus window idle timeout");
                    }
                    getCurMethodLocked().setStylusWindowIdleTimeoutForTest(j);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }
    }

    @GuardedBy({"ImfLock.class"})
    private void removeVirtualStylusIdForTestSessionLocked() {
        removeStylusDeviceIdLocked(VIRTUAL_STYLUS_ID_FOR_TEST.intValue());
    }

    private static IntArray getStylusInputDeviceIds(InputManager inputManager) {
        InputDevice inputDevice;
        IntArray intArray = new IntArray();
        for (int i : inputManager.getInputDeviceIds()) {
            if (inputManager.isInputDeviceEnabled(i) && (inputDevice = inputManager.getInputDevice(i)) != null && isStylusDevice(inputDevice)) {
                intArray.add(i);
            }
        }
        return intArray;
    }

    public void startProtoDump(byte[] bArr, int i, String str) {
        if (bArr != null || i == 2) {
            ImeTracing imeTracing = ImeTracing.getInstance();
            if (imeTracing.isAvailable() && imeTracing.isEnabled()) {
                ProtoOutputStream protoOutputStream = new ProtoOutputStream();
                if (i == 0) {
                    long start = protoOutputStream.start(2246267895810L);
                    protoOutputStream.write(1125281431553L, SystemClock.elapsedRealtimeNanos());
                    protoOutputStream.write(1138166333442L, str);
                    protoOutputStream.write(1146756268035L, bArr);
                    protoOutputStream.end(start);
                } else if (i == 1) {
                    long start2 = protoOutputStream.start(2246267895810L);
                    protoOutputStream.write(1125281431553L, SystemClock.elapsedRealtimeNanos());
                    protoOutputStream.write(1138166333442L, str);
                    protoOutputStream.write(1146756268035L, bArr);
                    protoOutputStream.end(start2);
                } else {
                    if (i != 2) {
                        return;
                    }
                    long start3 = protoOutputStream.start(2246267895810L);
                    protoOutputStream.write(1125281431553L, SystemClock.elapsedRealtimeNanos());
                    protoOutputStream.write(1138166333442L, str);
                    dumpDebug(protoOutputStream, 1146756268035L);
                    protoOutputStream.end(start3);
                }
                imeTracing.addToBuffer(protoOutputStream, i);
            }
        }
    }

    public boolean isImeTraceEnabled() {
        return ImeTracing.getInstance().isEnabled();
    }

    @EnforcePermission("android.permission.CONTROL_UI_TRACING")
    public void startImeTrace() {
        ArrayMap arrayMap;
        super.startImeTrace_enforcePermission();
        ImeTracing.getInstance().startTrace((PrintWriter) null);
        synchronized (ImfLock.class) {
            arrayMap = new ArrayMap(this.mClients);
        }
        for (ClientState clientState : arrayMap.values()) {
            if (clientState != null) {
                clientState.mClient.setImeTraceEnabled(true);
            }
        }
    }

    @EnforcePermission("android.permission.CONTROL_UI_TRACING")
    public void stopImeTrace() {
        ArrayMap arrayMap;
        super.stopImeTrace_enforcePermission();
        ImeTracing.getInstance().stopTrace((PrintWriter) null);
        synchronized (ImfLock.class) {
            arrayMap = new ArrayMap(this.mClients);
        }
        for (ClientState clientState : arrayMap.values()) {
            if (clientState != null) {
                clientState.mClient.setImeTraceEnabled(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        synchronized (ImfLock.class) {
            long start = protoOutputStream.start(j);
            protoOutputStream.write(1138166333441L, getSelectedMethodIdLocked());
            protoOutputStream.write(1120986464258L, getSequenceNumberLocked());
            protoOutputStream.write(1138166333443L, Objects.toString(this.mCurClient));
            protoOutputStream.write(1138166333444L, this.mWindowManagerInternal.getWindowName(this.mCurFocusedWindow));
            protoOutputStream.write(1138166333445L, this.mWindowManagerInternal.getWindowName(this.mLastImeTargetWindow));
            protoOutputStream.write(1138166333446L, InputMethodDebug.softInputModeToString(this.mCurFocusedWindowSoftInputMode));
            EditorInfo editorInfo = this.mCurEditorInfo;
            if (editorInfo != null) {
                editorInfo.dumpDebug(protoOutputStream, 1146756268039L);
            }
            protoOutputStream.write(1138166333448L, getCurIdLocked());
            this.mVisibilityStateComputer.dumpDebug(protoOutputStream, j);
            protoOutputStream.write(1133871366157L, this.mInFullscreenMode);
            protoOutputStream.write(1138166333454L, Objects.toString(getCurTokenLocked()));
            protoOutputStream.write(1120986464271L, this.mCurTokenDisplayId);
            protoOutputStream.write(1133871366160L, this.mSystemReady);
            protoOutputStream.write(1120986464273L, this.mLastSwitchUserId);
            protoOutputStream.write(1133871366162L, hasConnectionLocked());
            protoOutputStream.write(1133871366163L, this.mBoundToMethod);
            protoOutputStream.write(1133871366164L, this.mIsInteractive);
            protoOutputStream.write(1120986464277L, this.mBackDisposition);
            protoOutputStream.write(1120986464278L, this.mImeWindowVis);
            protoOutputStream.write(1133871366167L, this.mMenuController.getShowImeWithHardKeyboard());
            protoOutputStream.end(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyUserAction(IBinder iBinder) {
        if (DEBUG) {
            Slog.d(this.TAG, "Got the notification of a user action.");
        }
        synchronized (ImfLock.class) {
            if (getCurTokenLocked() != iBinder) {
                if (DEBUG) {
                    Slog.d(this.TAG, "Ignoring the user action notification from IMEs that are no longer active.");
                }
            } else {
                InputMethodInfo inputMethodInfo = this.mMethodMap.get(getSelectedMethodIdLocked());
                if (inputMethodInfo != null) {
                    this.mSwitchingController.onUserActionLocked(inputMethodInfo, this.mCurrentSubtype);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyImeVisibility(IBinder iBinder, IBinder iBinder2, boolean z, ImeTracker.Token token) {
        try {
            Trace.traceBegin(32L, "IMMS.applyImeVisibility");
            synchronized (ImfLock.class) {
                if (!calledWithValidTokenLocked(iBinder)) {
                    ImeTracker.forLogging().onFailed(token, 17);
                } else {
                    if (this.mImmsWrapper.getExtImpl().onApplyImeVisibility(z)) {
                        return;
                    }
                    this.mVisibilityApplier.applyImeVisibility(this.mVisibilityStateComputer.getWindowTokenFrom(iBinder2), token, z ? 1 : 0);
                }
            }
        } finally {
            Trace.traceEnd(32L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetStylusHandwriting(int i) {
        synchronized (ImfLock.class) {
            OptionalInt currentRequestId = this.mHwController.getCurrentRequestId();
            if (!currentRequestId.isPresent() || currentRequestId.getAsInt() != i) {
                Slog.w(this.TAG, "IME requested to finish handwriting with a mismatched requestId: " + i);
            }
            removeVirtualStylusIdForTestSessionLocked();
            scheduleResetStylusHandwriting();
        }
    }

    @GuardedBy({"ImfLock.class"})
    private void setInputMethodWithSubtypeIdLocked(IBinder iBinder, final String str, int i) {
        if (iBinder == null) {
            if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
                throw new SecurityException("Using null token requires permission android.permission.WRITE_SECURE_SETTINGS");
            }
        } else {
            if (getCurTokenLocked() != iBinder) {
                Slog.w(this.TAG, "Ignoring setInputMethod of uid " + Binder.getCallingUid() + " token: " + iBinder);
                return;
            }
            if (this.mMethodMap.get(str) != null && this.mSettings.getEnabledInputMethodListWithFilterLocked(new Predicate() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda2
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$setInputMethodWithSubtypeIdLocked$7;
                    lambda$setInputMethodWithSubtypeIdLocked$7 = InputMethodManagerService.lambda$setInputMethodWithSubtypeIdLocked$7(str, (InputMethodInfo) obj);
                    return lambda$setInputMethodWithSubtypeIdLocked$7;
                }
            }).isEmpty()) {
                throw new IllegalStateException("Requested IME is not enabled: " + str);
            }
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            setInputMethodLocked(str, i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$setInputMethodWithSubtypeIdLocked$7(String str, InputMethodInfo inputMethodInfo) {
        return inputMethodInfo.getId().equals(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void onShowHideSoftInputRequested(boolean z, IBinder iBinder, int i, ImeTracker.Token token) {
        WindowManagerInternal.ImeTargetInfo onToggleImeRequested = this.mWindowManagerInternal.onToggleImeRequested(z, this.mCurFocusedWindow, this.mVisibilityStateComputer.getWindowTokenFrom(iBinder), this.mCurTokenDisplayId);
        this.mSoftInputShowHideHistory.addEntry(new SoftInputShowHideHistory.Entry(this.mCurFocusedWindowClient, this.mCurFocusedWindowEditorInfo, onToggleImeRequested.focusedWindowName, this.mCurFocusedWindowSoftInputMode, i, this.mInFullscreenMode, onToggleImeRequested.requestWindowName, onToggleImeRequested.imeControlTargetName, onToggleImeRequested.imeLayerTargetName, onToggleImeRequested.imeSurfaceParentName));
        if (token != null) {
            this.mImeTrackerService.onImmsUpdate(token, onToggleImeRequested.requestWindowName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideMySoftInput(IBinder iBinder, int i, int i2) {
        try {
            Trace.traceBegin(32L, "IMMS.hideMySoftInput");
            synchronized (ImfLock.class) {
                if (calledWithValidTokenLocked(iBinder)) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        hideCurrentInputLocked(this.mLastImeTargetWindow, null, i, null, i2);
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            }
        } finally {
            Trace.traceEnd(32L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showMySoftInput(IBinder iBinder, int i) {
        try {
            Trace.traceBegin(32L, "IMMS.showMySoftInput");
            synchronized (ImfLock.class) {
                if (calledWithValidTokenLocked(iBinder)) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        showCurrentInputLocked(this.mLastImeTargetWindow, null, i, null, 3);
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            }
        } finally {
            Trace.traceEnd(32L);
        }
    }

    @VisibleForTesting
    ImeVisibilityApplier getVisibilityApplier() {
        DefaultImeVisibilityApplier defaultImeVisibilityApplier;
        synchronized (ImfLock.class) {
            defaultImeVisibilityApplier = this.mVisibilityApplier;
        }
        return defaultImeVisibilityApplier;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onApplyImeVisibilityFromComputer(IBinder iBinder, ImeVisibilityStateComputer.ImeVisibilityResult imeVisibilityResult) {
        synchronized (ImfLock.class) {
            this.mVisibilityApplier.applyImeVisibility(iBinder, null, imeVisibilityResult.getState(), imeVisibilityResult.getReason());
        }
    }

    @GuardedBy({"ImfLock.class"})
    void setEnabledSessionLocked(SessionState sessionState) {
        SessionState sessionState2 = this.mEnabledSession;
        if (sessionState2 != sessionState) {
            if (sessionState2 != null && sessionState2.mSession != null) {
                if (DEBUG) {
                    Slog.v(this.TAG, "Disabling: " + this.mEnabledSession);
                }
                SessionState sessionState3 = this.mEnabledSession;
                sessionState3.mMethod.setSessionEnabled(sessionState3.mSession, false);
            }
            this.mEnabledSession = sessionState;
            if (sessionState == null || sessionState.mSession == null) {
                return;
            }
            if (DEBUG) {
                Slog.v(this.TAG, "Enabling: " + this.mEnabledSession);
            }
            SessionState sessionState4 = this.mEnabledSession;
            sessionState4.mMethod.setSessionEnabled(sessionState4.mSession, true);
        }
    }

    @GuardedBy({"ImfLock.class"})
    void setEnabledSessionForAccessibilityLocked(SparseArray<AccessibilitySessionState> sparseArray) {
        AccessibilitySessionState valueAt;
        AccessibilitySessionState valueAt2;
        SparseArray sparseArray2 = new SparseArray();
        for (int i = 0; i < this.mEnabledAccessibilitySessions.size(); i++) {
            if (!sparseArray.contains(this.mEnabledAccessibilitySessions.keyAt(i)) && (valueAt2 = this.mEnabledAccessibilitySessions.valueAt(i)) != null) {
                sparseArray2.append(this.mEnabledAccessibilitySessions.keyAt(i), valueAt2.mSession);
            }
        }
        if (sparseArray2.size() > 0) {
            AccessibilityManagerInternal.get().setImeSessionEnabled(sparseArray2, false);
        }
        SparseArray sparseArray3 = new SparseArray();
        for (int i2 = 0; i2 < sparseArray.size(); i2++) {
            if (!this.mEnabledAccessibilitySessions.contains(sparseArray.keyAt(i2)) && (valueAt = sparseArray.valueAt(i2)) != null) {
                sparseArray3.append(sparseArray.keyAt(i2), valueAt.mSession);
            }
        }
        if (sparseArray3.size() > 0) {
            AccessibilityManagerInternal.get().setImeSessionEnabled(sparseArray3, true);
        }
        this.mEnabledAccessibilitySessions = sparseArray;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        ClientState clientState;
        SessionState sessionState;
        IInputMethodSession iInputMethodSession;
        switch (message.what) {
            case 1:
                int i = message.arg2;
                int i2 = message.arg1;
                if (i2 == 0) {
                    synchronized (ImfLock.class) {
                        r1 = isInputShown();
                    }
                } else if (i2 == 1) {
                    r1 = true;
                } else if (i2 != 2) {
                    Slog.e(this.TAG, "Unknown subtype picker mode = " + message.arg1);
                    return false;
                }
                IBinder iBinder = this.mCurFocusedWindow;
                if (iBinder != null && (clientState = this.mCurClient) != null && !isImeClientFocused(iBinder, clientState)) {
                    updateImeWindowStatus(true);
                    Slog.w(this.TAG, String.format("Ignoring showInputMethodMenu : %s", this.mCurClient));
                    return true;
                }
                this.mMenuController.showInputMethodMenu(r1, i);
                return true;
            case MSG_HIDE_CURRENT_INPUT_METHOD /* 1035 */:
                synchronized (ImfLock.class) {
                    hideCurrentInputLocked(this.mCurFocusedWindow, null, 0, null, ((Integer) message.obj).intValue());
                }
                return true;
            case MSG_REMOVE_IME_SURFACE /* 1060 */:
                synchronized (ImfLock.class) {
                    try {
                        SessionState sessionState2 = this.mEnabledSession;
                        if (sessionState2 != null && sessionState2.mSession != null && !isShowRequestedForCurrentWindow()) {
                            this.mEnabledSession.mSession.removeImeSurface();
                        }
                    } catch (RemoteException unused) {
                    }
                }
                return true;
            case MSG_REMOVE_IME_SURFACE_FROM_WINDOW /* 1061 */:
                IBinder iBinder2 = (IBinder) message.obj;
                synchronized (ImfLock.class) {
                    try {
                        if (iBinder2 == this.mCurFocusedWindow && (sessionState = this.mEnabledSession) != null && (iInputMethodSession = sessionState.mSession) != null) {
                            iInputMethodSession.removeImeSurface();
                        }
                    } catch (RemoteException unused2) {
                    }
                }
                return true;
            case MSG_UPDATE_IME_WINDOW_STATUS /* 1070 */:
                updateImeWindowStatus(message.arg1 == 1);
                return true;
            case MSG_RESET_HANDWRITING /* 1090 */:
                synchronized (ImfLock.class) {
                    if (this.mBindingController.supportsStylusHandwriting() && getCurMethodLocked() != null && hasSupportedStylusLocked()) {
                        Slog.d(this.TAG, "Initializing Handwriting Spy");
                        this.mHwController.initializeHandwritingSpy(this.mCurTokenDisplayId);
                    } else {
                        this.mHwController.reset();
                    }
                }
                return true;
            case MSG_START_HANDWRITING /* 1100 */:
                synchronized (ImfLock.class) {
                    IInputMethodInvoker curMethodLocked = getCurMethodLocked();
                    if (curMethodLocked != null && this.mCurFocusedWindow != null) {
                        HandwritingModeController.HandwritingSession startHandwritingSession = this.mHwController.startHandwritingSession(message.arg1, message.arg2, this.mBindingController.getCurMethodUid(), this.mCurFocusedWindow);
                        if (startHandwritingSession == null) {
                            Slog.e(this.TAG, "Failed to start handwriting session for requestId: " + message.arg1);
                            return true;
                        }
                        if (!curMethodLocked.startStylusHandwriting(startHandwritingSession.getRequestId(), startHandwritingSession.getHandwritingChannel(), startHandwritingSession.getRecordedEvents())) {
                            Slog.w(this.TAG, "Resetting handwriting mode.");
                            scheduleResetStylusHandwriting();
                        }
                        return true;
                    }
                    return true;
                }
            case MSG_FINISH_HANDWRITING /* 1110 */:
                synchronized (ImfLock.class) {
                    IInputMethodInvoker curMethodLocked2 = getCurMethodLocked();
                    if (curMethodLocked2 != null && this.mHwController.getCurrentRequestId().isPresent()) {
                        curMethodLocked2.finishStylusHandwriting();
                    }
                }
                return true;
            case MSG_REMOVE_HANDWRITING_WINDOW /* 1120 */:
                synchronized (ImfLock.class) {
                    IInputMethodInvoker curMethodLocked3 = getCurMethodLocked();
                    if (curMethodLocked3 != null) {
                        curMethodLocked3.removeStylusHandwritingWindow();
                    }
                }
                return true;
            case MSG_PREPARE_HANDWRITING_DELEGATION /* 1130 */:
                synchronized (ImfLock.class) {
                    Object obj = message.obj;
                    this.mHwController.prepareStylusHandwritingDelegation((String) ((Pair) obj).first, (String) ((Pair) obj).second);
                }
                return true;
            case MSG_SET_INTERACTIVE /* 3030 */:
                handleSetInteractive(message.arg1 != 0);
                return true;
            case MSG_HARD_KEYBOARD_SWITCH_CHANGED /* 4000 */:
                this.mMenuController.handleHardKeyboardStatusChange(message.arg1 == 1);
                synchronized (ImfLock.class) {
                    sendOnNavButtonFlagsChangedLocked();
                }
                return true;
            case 5000:
                onUnlockUser(message.arg1);
                return true;
            case MSG_DISPATCH_ON_INPUT_METHOD_LIST_UPDATED /* 5010 */:
                final int i3 = message.arg1;
                final List list = (List) message.obj;
                this.mInputMethodListListeners.forEach(new Consumer() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj2) {
                        ((InputMethodManagerInternal.InputMethodListListener) obj2).onInputMethodListUpdated(list, i3);
                    }
                });
                return true;
            case MSG_NOTIFY_IME_UID_TO_AUDIO_SERVICE /* 7000 */:
                if (this.mAudioManagerInternal == null) {
                    this.mAudioManagerInternal = (AudioManagerInternal) LocalServices.getService(AudioManagerInternal.class);
                }
                AudioManagerInternal audioManagerInternal = this.mAudioManagerInternal;
                if (audioManagerInternal != null) {
                    audioManagerInternal.setInputMethodServiceUid(message.arg1);
                }
                return true;
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStylusHandwritingReady(int i, int i2) {
        this.mHandler.obtainMessage(MSG_START_HANDWRITING, i, i2).sendToTarget();
    }

    private void handleSetInteractive(boolean z) {
        synchronized (ImfLock.class) {
            this.mIsInteractive = z;
            updateSystemUiLocked(z ? this.mImeWindowVis : 0, this.mBackDisposition);
            ClientState clientState = this.mCurClient;
            if (clientState != null && clientState.mClient != null) {
                if (this.mImePlatformCompatUtils.shouldUseSetInteractiveProtocol(getCurMethodUidLocked())) {
                    ImeVisibilityStateComputer.ImeVisibilityResult onInteractiveChanged = this.mVisibilityStateComputer.onInteractiveChanged(this.mCurFocusedWindow, z);
                    if (onInteractiveChanged != null) {
                        this.mVisibilityApplier.applyImeVisibility(this.mCurFocusedWindow, null, onInteractiveChanged.getState(), onInteractiveChanged.getReason());
                    }
                    this.mCurClient.mClient.setInteractive(this.mIsInteractive, this.mInFullscreenMode);
                } else {
                    this.mCurClient.mClient.setActive(this.mIsInteractive, this.mInFullscreenMode);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public boolean chooseNewDefaultIMELocked() {
        InputMethodInfo mostApplicableDefaultIME = InputMethodInfoUtils.getMostApplicableDefaultIME(this.mSettings.getEnabledInputMethodListLocked());
        InputMethodInfo defaultInputMethodByConfig = this.mImmsWrapper.getExtImpl().getDefaultInputMethodByConfig(this.mSettings.getCurrentUserId());
        if (defaultInputMethodByConfig != null) {
            mostApplicableDefaultIME = defaultInputMethodByConfig;
        }
        if (mostApplicableDefaultIME == null) {
            return false;
        }
        if (DEBUG) {
            Slog.d(this.TAG, "New default IME was selected: " + mostApplicableDefaultIME.getId());
        }
        resetSelectedInputMethodAndSubtypeLocked(mostApplicableDefaultIME.getId());
        return true;
    }

    void queryInputMethodServicesInternal(Context context, int i, ArrayMap<String, List<InputMethodSubtype>> arrayMap, ArrayMap<String, InputMethodInfo> arrayMap2, ArrayList<InputMethodInfo> arrayList, int i2, List<String> list) {
        if (context.getUserId() != i) {
            context = context.createContextAsUser(UserHandle.of(i), 0);
        }
        Context context2 = context;
        arrayList.clear();
        arrayMap2.clear();
        int i3 = 268435456;
        if (i2 != 0) {
            if (i2 != 1) {
                Slog.e(this.TAG, "Unknown directBootAwareness=" + i2 + ". Falling back to DirectBootAwareness.AUTO");
            } else {
                i3 = 786432;
            }
        }
        List<ResolveInfo> queryIntentServices = context2.getPackageManager().queryIntentServices(new Intent("android.view.InputMethod"), PackageManager.ResolveInfoFlags.of(i3 | 32896 | 1073741824));
        arrayList.ensureCapacity(queryIntentServices.size());
        arrayMap2.ensureCapacity(queryIntentServices.size());
        filterInputMethodServices(arrayMap, arrayMap2, arrayList, list, context2, queryIntentServices);
    }

    void filterInputMethodServices(ArrayMap<String, List<InputMethodSubtype>> arrayMap, ArrayMap<String, InputMethodInfo> arrayMap2, ArrayList<InputMethodInfo> arrayList, List<String> list, Context context, List<ResolveInfo> list2) {
        ArrayMap arrayMap3 = new ArrayMap();
        for (int i = 0; i < list2.size(); i++) {
            ResolveInfo resolveInfo = list2.get(i);
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            String computeId = InputMethodInfo.computeId(resolveInfo);
            if (!"android.permission.BIND_INPUT_METHOD".equals(serviceInfo.permission)) {
                Slog.w(this.TAG, "Skipping input method " + computeId + ": it does not require the permission android.permission.BIND_INPUT_METHOD");
            } else if (!this.mImmsWrapper.getExtImpl().onInputMethodQueried(serviceInfo.packageName, computeId)) {
                if (DEBUG) {
                    Slog.d(this.TAG, "Checking " + computeId);
                }
                try {
                    InputMethodInfo inputMethodInfo = new InputMethodInfo(context, resolveInfo, arrayMap.get(computeId));
                    if (!inputMethodInfo.isVrOnly()) {
                        String str = serviceInfo.packageName;
                        if (!serviceInfo.applicationInfo.isSystemApp() && !list.contains(inputMethodInfo.getId()) && ((Integer) arrayMap3.getOrDefault(str, 0)).intValue() >= 20) {
                            if (DEBUG) {
                                Slog.d(this.TAG, "Found an input method, but ignored due threshold: " + inputMethodInfo);
                            }
                        }
                        arrayMap3.put(str, Integer.valueOf(((Integer) arrayMap3.getOrDefault(str, 0)).intValue() + 1));
                        arrayList.add(inputMethodInfo);
                        arrayMap2.put(inputMethodInfo.getId(), inputMethodInfo);
                        if (DEBUG) {
                            Slog.d(this.TAG, "Found an input method " + inputMethodInfo);
                        }
                    }
                } catch (Exception e) {
                    Slog.wtf(this.TAG, "Unable to load input method " + computeId, e);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x0141  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x010b  */
    @GuardedBy({"ImfLock.class"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void buildInputMethodListLocked(boolean z) {
        boolean z2;
        int size;
        int i;
        String selectedInputMethod;
        boolean z3;
        if (DEBUG) {
            Slog.d(this.TAG, "--- re-buildInputMethodList reset = " + z + " \n ------ caller=" + Debug.getCallers(10));
        }
        if (!this.mSystemReady) {
            Slog.e(this.TAG, "buildInputMethodListLocked is not allowed until system is ready");
            return;
        }
        this.mMethodMapUpdateCount++;
        this.mMyPackageMonitor.clearKnownImePackageNamesLocked();
        queryInputMethodServicesInternal(this.mContext, this.mSettings.getCurrentUserId(), this.mAdditionalSubtypeMap, this.mMethodMap, this.mMethodList, 0, this.mSettings.getEnabledInputMethodNames());
        this.mImmsWrapper.getExtImpl().configInputMethodAfterQuery();
        List queryIntentServicesAsUser = this.mContext.getPackageManager().queryIntentServicesAsUser(new Intent("android.view.InputMethod"), 1073742336, this.mSettings.getCurrentUserId());
        int size2 = queryIntentServicesAsUser.size();
        for (int i2 = 0; i2 < size2; i2++) {
            ServiceInfo serviceInfo = ((ResolveInfo) queryIntentServicesAsUser.get(i2)).serviceInfo;
            if ("android.permission.BIND_INPUT_METHOD".equals(serviceInfo.permission)) {
                this.mMyPackageMonitor.addKnownImePackageNameLocked(serviceInfo.packageName);
            }
        }
        if (!z) {
            ArrayList<InputMethodInfo> enabledInputMethodListLocked = this.mSettings.getEnabledInputMethodListLocked();
            int size3 = enabledInputMethodListLocked.size();
            int i3 = 0;
            boolean z4 = false;
            while (true) {
                if (i3 >= size3) {
                    z3 = false;
                    break;
                }
                InputMethodInfo inputMethodInfo = enabledInputMethodListLocked.get(i3);
                if (this.mMethodList.contains(inputMethodInfo)) {
                    if (!inputMethodInfo.isAuxiliaryIme()) {
                        z3 = true;
                        z4 = true;
                        break;
                    }
                    z4 = true;
                }
                i3++;
            }
            if (!z4) {
                if (DEBUG) {
                    Slog.i(this.TAG, "All the enabled IMEs are gone. Reset default enabled IMEs.");
                }
                resetSelectedInputMethodAndSubtypeLocked("");
                z = true;
            } else if (!z3) {
                if (DEBUG) {
                    Slog.i(this.TAG, "All the enabled non-Aux IMEs are gone. Do partial reset.");
                }
                z2 = true;
                if (!z || z2) {
                    ArrayList<InputMethodInfo> defaultEnabledImes = InputMethodInfoUtils.getDefaultEnabledImes(this.mContext, this.mMethodList, z2);
                    this.mImmsWrapper.getExtImpl().updateDefaultEnabledImes(defaultEnabledImes);
                    size = defaultEnabledImes.size();
                    for (i = 0; i < size; i++) {
                        InputMethodInfo inputMethodInfo2 = defaultEnabledImes.get(i);
                        if (DEBUG) {
                            Slog.d(this.TAG, "--- enable ime = " + inputMethodInfo2);
                        }
                        setInputMethodEnabledLocked(inputMethodInfo2.getId(), true);
                    }
                }
                selectedInputMethod = this.mSettings.getSelectedInputMethod();
                if (!TextUtils.isEmpty(selectedInputMethod)) {
                    if (!this.mMethodMap.containsKey(selectedInputMethod)) {
                        Slog.w(this.TAG, "Default IME is uninstalled. Choose new default IME.");
                        if (chooseNewDefaultIMELocked()) {
                            updateInputMethodsFromSettingsLocked(true);
                        }
                    } else {
                        setInputMethodEnabledLocked(selectedInputMethod, true);
                    }
                }
                updateDefaultVoiceImeIfNeededLocked();
                this.mSwitchingController.resetCircularListLocked(this.mContext);
                this.mHardwareKeyboardShortcutController.reset(this.mSettings);
                sendOnNavButtonFlagsChangedLocked();
                this.mHandler.obtainMessage(MSG_DISPATCH_ON_INPUT_METHOD_LIST_UPDATED, this.mSettings.getCurrentUserId(), 0, new ArrayList(this.mMethodList)).sendToTarget();
            }
        }
        z2 = false;
        if (!z) {
        }
        ArrayList<InputMethodInfo> defaultEnabledImes2 = InputMethodInfoUtils.getDefaultEnabledImes(this.mContext, this.mMethodList, z2);
        this.mImmsWrapper.getExtImpl().updateDefaultEnabledImes(defaultEnabledImes2);
        size = defaultEnabledImes2.size();
        while (i < size) {
        }
        selectedInputMethod = this.mSettings.getSelectedInputMethod();
        if (!TextUtils.isEmpty(selectedInputMethod)) {
        }
        updateDefaultVoiceImeIfNeededLocked();
        this.mSwitchingController.resetCircularListLocked(this.mContext);
        this.mHardwareKeyboardShortcutController.reset(this.mSettings);
        sendOnNavButtonFlagsChangedLocked();
        this.mHandler.obtainMessage(MSG_DISPATCH_ON_INPUT_METHOD_LIST_UPDATED, this.mSettings.getCurrentUserId(), 0, new ArrayList(this.mMethodList)).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void sendOnNavButtonFlagsChangedLocked() {
        IInputMethodInvoker curMethod = this.mBindingController.getCurMethod();
        if (curMethod == null) {
            return;
        }
        curMethod.onNavButtonFlagsChanged(getInputMethodNavButtonFlagsLocked());
    }

    @GuardedBy({"ImfLock.class"})
    private void updateDefaultVoiceImeIfNeededLocked() {
        String string = this.mContext.getString(R.string.CfMmi);
        String defaultVoiceInputMethod = this.mSettings.getDefaultVoiceInputMethod();
        InputMethodInfo chooseSystemVoiceIme = InputMethodInfoUtils.chooseSystemVoiceIme(this.mMethodMap, string, defaultVoiceInputMethod);
        if (chooseSystemVoiceIme == null) {
            if (DEBUG) {
                Slog.i(this.TAG, "Found no valid default Voice IME. If the user is still locked, this may be expected.");
            }
            if (TextUtils.isEmpty(defaultVoiceInputMethod)) {
                return;
            }
            this.mSettings.putDefaultVoiceInputMethod("");
            return;
        }
        if (TextUtils.equals(defaultVoiceInputMethod, chooseSystemVoiceIme.getId())) {
            return;
        }
        if (DEBUG) {
            Slog.i(this.TAG, "Enabling the default Voice IME:" + chooseSystemVoiceIme);
        }
        setInputMethodEnabledLocked(chooseSystemVoiceIme.getId(), true);
        this.mSettings.putDefaultVoiceInputMethod(chooseSystemVoiceIme.getId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public boolean setInputMethodEnabledLocked(String str, boolean z) {
        List<Pair<String, ArrayList<String>>> enabledInputMethodsAndSubtypeListLocked = this.mSettings.getEnabledInputMethodsAndSubtypeListLocked();
        if (z) {
            Iterator<Pair<String, ArrayList<String>>> it = enabledInputMethodsAndSubtypeListLocked.iterator();
            while (it.hasNext()) {
                if (((String) it.next().first).equals(str)) {
                    return true;
                }
            }
            this.mSettings.appendAndPutEnabledInputMethodLocked(str, false);
            return false;
        }
        if (!this.mSettings.buildAndPutEnabledInputMethodsStrRemovingIdLocked(new StringBuilder(), enabledInputMethodsAndSubtypeListLocked, str)) {
            return false;
        }
        if (str.equals(this.mSettings.getSelectedInputMethod()) && !chooseNewDefaultIMELocked()) {
            Slog.i(this.TAG, "Can't find new IME, unsetting the current input method.");
            resetSelectedInputMethodAndSubtypeLocked("");
        }
        return true;
    }

    @GuardedBy({"ImfLock.class"})
    private void setSelectedInputMethodAndSubtypeLocked(InputMethodInfo inputMethodInfo, int i, boolean z) {
        this.mSettings.saveCurrentInputMethodAndSubtypeToHistory(getSelectedMethodIdLocked(), this.mCurrentSubtype);
        if (inputMethodInfo == null || i < 0) {
            this.mSettings.putSelectedSubtype(-1);
            this.mCurrentSubtype = null;
        } else if (i < inputMethodInfo.getSubtypeCount()) {
            InputMethodSubtype subtypeAt = inputMethodInfo.getSubtypeAt(i);
            this.mSettings.putSelectedSubtype(subtypeAt.hashCode());
            this.mCurrentSubtype = subtypeAt;
        } else {
            this.mSettings.putSelectedSubtype(-1);
            this.mCurrentSubtype = getCurrentInputMethodSubtypeLocked();
        }
        notifyInputMethodSubtypeChangedLocked(this.mSettings.getCurrentUserId(), inputMethodInfo, this.mCurrentSubtype);
        if (z) {
            return;
        }
        this.mSettings.putSelectedInputMethod(inputMethodInfo != null ? inputMethodInfo.getId() : "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x0010, code lost:
    
        r6 = r5.mSettings.getLastSubtypeForInputMethodLocked(r6);
     */
    @GuardedBy({"ImfLock.class"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void resetSelectedInputMethodAndSubtypeLocked(String str) {
        int i;
        String lastSubtypeForInputMethodLocked;
        InputMethodInfo inputMethodInfo = this.mMethodMap.get(str);
        if (inputMethodInfo != null && !TextUtils.isEmpty(str) && lastSubtypeForInputMethodLocked != null) {
            try {
                i = SubtypeUtils.getSubtypeIdFromHashCode(inputMethodInfo, Integer.parseInt(lastSubtypeForInputMethodLocked));
            } catch (NumberFormatException e) {
                Slog.w(this.TAG, "HashCode for subtype looks broken: " + lastSubtypeForInputMethodLocked, e);
            }
            setSelectedInputMethodAndSubtypeLocked(inputMethodInfo, i, false);
        }
        i = -1;
        setSelectedInputMethodAndSubtypeLocked(inputMethodInfo, i, false);
    }

    public InputMethodSubtype getCurrentInputMethodSubtype(int i) {
        if (UserHandle.getCallingUserId() != i) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (ImfLock.class) {
            if (this.mSettings.getCurrentUserId() == i) {
                return getCurrentInputMethodSubtypeLocked();
            }
            return new InputMethodUtils.InputMethodSettings(this.mContext, queryMethodMapForUser(i), i, false).getCurrentInputMethodSubtypeForNonCurrentUsers();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public InputMethodSubtype getCurrentInputMethodSubtypeLocked() {
        InputMethodSubtype inputMethodSubtype;
        String selectedMethodIdLocked = getSelectedMethodIdLocked();
        if (selectedMethodIdLocked == null) {
            return null;
        }
        boolean isSubtypeSelected = this.mSettings.isSubtypeSelected();
        InputMethodInfo inputMethodInfo = this.mMethodMap.get(selectedMethodIdLocked);
        if (inputMethodInfo == null || inputMethodInfo.getSubtypeCount() == 0) {
            return null;
        }
        if (!isSubtypeSelected || (inputMethodSubtype = this.mCurrentSubtype) == null || !SubtypeUtils.isValidSubtypeId(inputMethodInfo, inputMethodSubtype.hashCode())) {
            int selectedInputMethodSubtypeId = this.mSettings.getSelectedInputMethodSubtypeId(selectedMethodIdLocked);
            if (selectedInputMethodSubtypeId == -1) {
                List<InputMethodSubtype> enabledInputMethodSubtypeListLocked = this.mSettings.getEnabledInputMethodSubtypeListLocked(inputMethodInfo, true);
                if (enabledInputMethodSubtypeListLocked.size() == 1) {
                    this.mCurrentSubtype = enabledInputMethodSubtypeListLocked.get(0);
                } else if (enabledInputMethodSubtypeListLocked.size() > 1) {
                    InputMethodSubtype findLastResortApplicableSubtypeLocked = SubtypeUtils.findLastResortApplicableSubtypeLocked(this.mRes, enabledInputMethodSubtypeListLocked, "keyboard", null, true);
                    this.mCurrentSubtype = findLastResortApplicableSubtypeLocked;
                    if (findLastResortApplicableSubtypeLocked == null) {
                        this.mCurrentSubtype = SubtypeUtils.findLastResortApplicableSubtypeLocked(this.mRes, enabledInputMethodSubtypeListLocked, null, null, true);
                    }
                }
            } else {
                this.mCurrentSubtype = SubtypeUtils.getSubtypes(inputMethodInfo).get(selectedInputMethodSubtypeId);
            }
        }
        return this.mCurrentSubtype;
    }

    @GuardedBy({"ImfLock.class"})
    private InputMethodInfo queryDefaultInputMethodForUserIdLocked(int i) {
        InputMethodInfo inputMethodInfo;
        String selectedInputMethodForUser = this.mSettings.getSelectedInputMethodForUser(i);
        if (TextUtils.isEmpty(selectedInputMethodForUser)) {
            Slog.e(this.TAG, "No default input method found for userId " + i);
            return null;
        }
        if (i == this.mSettings.getCurrentUserId() && (inputMethodInfo = this.mMethodMap.get(selectedInputMethodForUser)) != null) {
            return new InputMethodInfo(inputMethodInfo);
        }
        ArrayMap arrayMap = new ArrayMap();
        AdditionalSubtypeUtils.load(arrayMap, i);
        Context createContextAsUser = this.mContext.createContextAsUser(UserHandle.of(i), 0);
        for (ResolveInfo resolveInfo : createContextAsUser.getPackageManager().queryIntentServicesAsUser(new Intent("android.view.InputMethod"), PackageManager.ResolveInfoFlags.of(268468352L), i)) {
            if (selectedInputMethodForUser.equals(InputMethodInfo.computeId(resolveInfo))) {
                try {
                    return new InputMethodInfo(createContextAsUser, resolveInfo, (List) arrayMap.get(selectedInputMethodForUser));
                } catch (Exception e) {
                    Slog.wtf(this.TAG, "Unable to load input method " + selectedInputMethodForUser, e);
                }
            }
        }
        Slog.e(this.TAG, "Error while locating input method info for imeId: " + selectedInputMethodForUser);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayMap<String, InputMethodInfo> queryMethodMapForUser(int i) {
        ArrayMap<String, InputMethodInfo> arrayMap = new ArrayMap<>();
        ArrayList<InputMethodInfo> arrayList = new ArrayList<>();
        ArrayMap<String, List<InputMethodSubtype>> arrayMap2 = new ArrayMap<>();
        AdditionalSubtypeUtils.load(arrayMap2, i);
        queryInputMethodServicesInternal(this.mContext, i, arrayMap2, arrayMap, arrayList, 0, this.mSettings.getEnabledInputMethodNames());
        return arrayMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public boolean switchToInputMethodLocked(String str, int i) {
        if (i == this.mSettings.getCurrentUserId()) {
            if (!this.mMethodMap.containsKey(str) || !this.mSettings.getEnabledInputMethodListLocked().contains(this.mMethodMap.get(str))) {
                return false;
            }
            setInputMethodLocked(str, -1);
            return true;
        }
        ArrayMap<String, InputMethodInfo> queryMethodMapForUser = queryMethodMapForUser(i);
        InputMethodUtils.InputMethodSettings inputMethodSettings = new InputMethodUtils.InputMethodSettings(this.mContext, queryMethodMapForUser, i, false);
        if (!queryMethodMapForUser.containsKey(str) || !inputMethodSettings.getEnabledInputMethodListLocked().contains(queryMethodMapForUser.get(str))) {
            return false;
        }
        inputMethodSettings.putSelectedInputMethod(str);
        inputMethodSettings.putSelectedSubtype(-1);
        return true;
    }

    private boolean canCallerAccessInputMethod(String str, int i, int i2, InputMethodUtils.InputMethodSettings inputMethodSettings) {
        String selectedInputMethod = inputMethodSettings.getSelectedInputMethod();
        ComponentName convertIdToComponentName = selectedInputMethod != null ? InputMethodUtils.convertIdToComponentName(selectedInputMethod) : null;
        if (convertIdToComponentName != null && convertIdToComponentName.getPackageName().equals(str)) {
            return true;
        }
        boolean z = !this.mPackageManagerInternal.filterAppAccess(str, i, i2);
        if (DEBUG && !z) {
            Slog.d(this.TAG, "Input method " + str + " is not visible to the caller " + i);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void publishLocalService() {
        LocalServices.addService(InputMethodManagerInternal.class, new LocalServiceImpl());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class LocalServiceImpl extends InputMethodManagerInternal {
        private LocalServiceImpl() {
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void setInteractive(boolean z) {
            InputMethodManagerService.this.mImmsWrapper.getExtImpl().logMethodCallers("setInteractive = " + z);
            InputMethodManagerService.this.mHandler.obtainMessage(InputMethodManagerService.MSG_SET_INTERACTIVE, z ? 1 : 0, 0).sendToTarget();
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void hideCurrentInputMethod(int i) {
            InputMethodManagerService.this.mImmsWrapper.getExtImpl().logMethodCallers("hideCurrentInputMethod");
            InputMethodManagerService.this.mHandler.removeMessages(InputMethodManagerService.MSG_HIDE_CURRENT_INPUT_METHOD);
            InputMethodManagerService.this.mHandler.obtainMessage(InputMethodManagerService.MSG_HIDE_CURRENT_INPUT_METHOD, Integer.valueOf(i)).sendToTarget();
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public List<InputMethodInfo> getInputMethodListAsUser(int i) {
            List<InputMethodInfo> inputMethodListLocked;
            synchronized (ImfLock.class) {
                inputMethodListLocked = InputMethodManagerService.this.getInputMethodListLocked(i, 0, 1000);
            }
            return inputMethodListLocked;
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public List<InputMethodInfo> getEnabledInputMethodListAsUser(int i) {
            List<InputMethodInfo> enabledInputMethodListLocked;
            synchronized (ImfLock.class) {
                enabledInputMethodListLocked = InputMethodManagerService.this.getEnabledInputMethodListLocked(i, 1000);
            }
            return enabledInputMethodListLocked;
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void onCreateInlineSuggestionsRequest(int i, InlineSuggestionsRequestInfo inlineSuggestionsRequestInfo, IInlineSuggestionsRequestCallback iInlineSuggestionsRequestCallback) {
            boolean isTouchExplorationEnabled = AccessibilityManagerInternal.get().isTouchExplorationEnabled(i);
            synchronized (ImfLock.class) {
                InputMethodManagerService.this.mAutofillController.onCreateInlineSuggestionsRequest(i, inlineSuggestionsRequestInfo, iInlineSuggestionsRequestCallback, isTouchExplorationEnabled);
            }
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public boolean switchToInputMethod(String str, int i) {
            boolean switchToInputMethodLocked;
            synchronized (ImfLock.class) {
                switchToInputMethodLocked = InputMethodManagerService.this.switchToInputMethodLocked(str, i);
            }
            return switchToInputMethodLocked;
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public boolean setInputMethodEnabled(String str, boolean z, int i) {
            synchronized (ImfLock.class) {
                if (i == InputMethodManagerService.this.mSettings.getCurrentUserId()) {
                    if (!InputMethodManagerService.this.mMethodMap.containsKey(str)) {
                        return false;
                    }
                    InputMethodManagerService.this.setInputMethodEnabledLocked(str, z);
                    return true;
                }
                ArrayMap queryMethodMapForUser = InputMethodManagerService.this.queryMethodMapForUser(i);
                InputMethodUtils.InputMethodSettings inputMethodSettings = new InputMethodUtils.InputMethodSettings(InputMethodManagerService.this.mContext, queryMethodMapForUser, i, false);
                if (!queryMethodMapForUser.containsKey(str)) {
                    return false;
                }
                if (z) {
                    if (!inputMethodSettings.getEnabledInputMethodListLocked().contains(queryMethodMapForUser.get(str))) {
                        inputMethodSettings.appendAndPutEnabledInputMethodLocked(str, false);
                    }
                } else {
                    inputMethodSettings.buildAndPutEnabledInputMethodsStrRemovingIdLocked(new StringBuilder(), inputMethodSettings.getEnabledInputMethodsAndSubtypeListLocked(), str);
                }
                return true;
            }
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void registerInputMethodListListener(InputMethodManagerInternal.InputMethodListListener inputMethodListListener) {
            InputMethodManagerService.this.mInputMethodListListeners.addIfAbsent(inputMethodListListener);
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public boolean transferTouchFocusToImeWindow(IBinder iBinder, int i) {
            synchronized (ImfLock.class) {
                if (i == InputMethodManagerService.this.mCurTokenDisplayId && InputMethodManagerService.this.mCurHostInputToken != null) {
                    return InputMethodManagerService.this.mInputManagerInternal.transferTouchFocus(iBinder, InputMethodManagerService.this.mCurHostInputToken);
                }
                return false;
            }
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void reportImeControl(IBinder iBinder) {
            synchronized (ImfLock.class) {
                InputMethodManagerService inputMethodManagerService = InputMethodManagerService.this;
                if (inputMethodManagerService.mCurFocusedWindow != iBinder) {
                    inputMethodManagerService.mCurPerceptible = true;
                }
            }
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void onImeParentChanged() {
            synchronized (ImfLock.class) {
                InputMethodManagerService inputMethodManagerService = InputMethodManagerService.this;
                if (inputMethodManagerService.mLastImeTargetWindow != inputMethodManagerService.mCurFocusedWindow) {
                    inputMethodManagerService.mMenuController.hideInputMethodMenu();
                }
            }
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void removeImeSurface() {
            InputMethodManagerService.this.mHandler.obtainMessage(InputMethodManagerService.MSG_REMOVE_IME_SURFACE).sendToTarget();
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void updateImeWindowStatus(boolean z) {
            InputMethodManagerService.this.mImmsWrapper.getExtImpl().logMethodCallers("updateImeWindowStatus = " + z);
            InputMethodManagerService.this.mHandler.obtainMessage(InputMethodManagerService.MSG_UPDATE_IME_WINDOW_STATUS, z ? 1 : 0, 0).sendToTarget();
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void onSessionForAccessibilityCreated(int i, IAccessibilityInputMethodSession iAccessibilityInputMethodSession) {
            synchronized (ImfLock.class) {
                if (InputMethodManagerService.this.mCurClient != null) {
                    InputMethodManagerService inputMethodManagerService = InputMethodManagerService.this;
                    inputMethodManagerService.clearClientSessionForAccessibilityLocked(inputMethodManagerService.mCurClient, i);
                    InputMethodManagerService.this.mCurClient.mAccessibilitySessions.put(i, new AccessibilitySessionState(InputMethodManagerService.this.mCurClient, i, iAccessibilityInputMethodSession));
                    InputMethodManagerService.this.attachNewAccessibilityLocked(11, true);
                    SessionState sessionState = InputMethodManagerService.this.mCurClient.mCurSession;
                    IInputMethodSession iInputMethodSession = sessionState == null ? null : sessionState.mSession;
                    InputMethodManagerService inputMethodManagerService2 = InputMethodManagerService.this;
                    InputMethodManagerService.this.mCurClient.mClient.onBindAccessibilityService(new InputBindResult(16, iInputMethodSession, inputMethodManagerService2.createAccessibilityInputMethodSessions(inputMethodManagerService2.mCurClient.mAccessibilitySessions), (InputChannel) null, InputMethodManagerService.this.getCurIdLocked(), InputMethodManagerService.this.getSequenceNumberLocked(), InputMethodManagerService.this.mCurVirtualDisplayToScreenMatrix, false), i);
                }
            }
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void unbindAccessibilityFromCurrentClient(int i) {
            synchronized (ImfLock.class) {
                if (InputMethodManagerService.this.mCurClient != null) {
                    if (InputMethodManagerService.DEBUG) {
                        Slog.v(InputMethodManagerService.this.TAG, "unbindAccessibilityFromCurrentClientLocked: client=" + InputMethodManagerService.this.mCurClient.mClient.asBinder());
                    }
                    InputMethodManagerService.this.mCurClient.mClient.onUnbindAccessibilityService(InputMethodManagerService.this.getSequenceNumberLocked(), i);
                }
                if (InputMethodManagerService.this.getCurMethodLocked() != null) {
                    int size = InputMethodManagerService.this.mClients.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        InputMethodManagerService inputMethodManagerService = InputMethodManagerService.this;
                        inputMethodManagerService.clearClientSessionForAccessibilityLocked(inputMethodManagerService.mClients.valueAt(i2), i);
                    }
                    AccessibilitySessionState accessibilitySessionState = InputMethodManagerService.this.mEnabledAccessibilitySessions.get(i);
                    if (accessibilitySessionState != null) {
                        InputMethodManagerService.this.finishSessionForAccessibilityLocked(accessibilitySessionState);
                        InputMethodManagerService.this.mEnabledAccessibilitySessions.remove(i);
                    }
                }
            }
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void maybeFinishStylusHandwriting() {
            InputMethodManagerService.this.mHandler.removeMessages(InputMethodManagerService.MSG_FINISH_HANDWRITING);
            InputMethodManagerService.this.mHandler.obtainMessage(InputMethodManagerService.MSG_FINISH_HANDWRITING).sendToTarget();
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void switchKeyboardLayout(int i) {
            synchronized (ImfLock.class) {
                InputMethodManagerService inputMethodManagerService = InputMethodManagerService.this;
                InputMethodInfo inputMethodInfo = inputMethodManagerService.mMethodMap.get(inputMethodManagerService.getSelectedMethodIdLocked());
                if (inputMethodInfo == null) {
                    return;
                }
                InputMethodSubtypeHandle onSubtypeSwitch = InputMethodManagerService.this.mHardwareKeyboardShortcutController.onSubtypeSwitch(InputMethodSubtypeHandle.of(inputMethodInfo, InputMethodManagerService.this.mCurrentSubtype), i > 0);
                if (onSubtypeSwitch == null) {
                    return;
                }
                InputMethodInfo inputMethodInfo2 = InputMethodManagerService.this.mMethodMap.get(onSubtypeSwitch.getImeId());
                if (inputMethodInfo2 == null) {
                    return;
                }
                int subtypeCount = inputMethodInfo2.getSubtypeCount();
                if (subtypeCount == 0) {
                    if (onSubtypeSwitch.equals(InputMethodSubtypeHandle.of(inputMethodInfo2, (InputMethodSubtype) null))) {
                        InputMethodManagerService.this.setInputMethodLocked(inputMethodInfo2.getId(), -1);
                    }
                    return;
                }
                for (int i2 = 0; i2 < subtypeCount; i2++) {
                    if (onSubtypeSwitch.equals(InputMethodSubtypeHandle.of(inputMethodInfo2, inputMethodInfo2.getSubtypeAt(i2)))) {
                        InputMethodManagerService.this.setInputMethodLocked(inputMethodInfo2.getId(), i2);
                        return;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IInputContentUriToken createInputContentUriToken(IBinder iBinder, Uri uri, String str) {
        if (iBinder == null) {
            throw new NullPointerException("token");
        }
        if (str == null) {
            throw new NullPointerException(DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        }
        if (uri == null) {
            throw new NullPointerException("contentUri");
        }
        if (!"content".equals(uri.getScheme())) {
            throw new InvalidParameterException("contentUri must have content scheme");
        }
        synchronized (ImfLock.class) {
            int callingUid = Binder.getCallingUid();
            if (getSelectedMethodIdLocked() == null) {
                return null;
            }
            if (getCurTokenLocked() != iBinder) {
                Slog.e(this.TAG, "Ignoring createInputContentUriToken mCurToken=" + getCurTokenLocked() + " token=" + iBinder);
                return null;
            }
            EditorInfo editorInfo = this.mCurEditorInfo;
            String str2 = editorInfo != null ? editorInfo.packageName : null;
            if (!TextUtils.equals(str2, str)) {
                Slog.e(this.TAG, "Ignoring createInputContentUriToken mCurEditorInfo.packageName=" + str2 + " packageName=" + str);
                return null;
            }
            return new InputContentUriTokenHandler(ContentProvider.getUriWithoutUserId(uri), callingUid, str, ContentProvider.getUserIdFromUri(uri, UserHandle.getUserId(callingUid)), UserHandle.getUserId(this.mCurClient.mUid));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportFullscreenMode(IBinder iBinder, boolean z) {
        IInputMethodClientInvoker iInputMethodClientInvoker;
        synchronized (ImfLock.class) {
            if (calledWithValidTokenLocked(iBinder)) {
                ClientState clientState = this.mCurClient;
                if (clientState != null && (iInputMethodClientInvoker = clientState.mClient) != null) {
                    this.mInFullscreenMode = z;
                    iInputMethodClientInvoker.reportFullscreenMode(z);
                }
            }
        }
    }

    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, this.TAG, printWriter)) {
            this.mImmsWrapper.getExtImpl().configDebug(fileDescriptor, strArr);
            PriorityDump.dump(this.mPriorityDumper, fileDescriptor, printWriter, strArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpAsStringNoCheck(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z) {
        ClientState clientState;
        ClientState clientState2;
        IInputMethodInvoker curMethodLocked;
        PrintWriterPrinter printWriterPrinter = new PrintWriterPrinter(printWriter);
        synchronized (ImfLock.class) {
            printWriterPrinter.println("Current Input Method Manager state:");
            int size = this.mMethodList.size();
            printWriterPrinter.println("  Input Methods: mMethodMapUpdateCount=" + this.mMethodMapUpdateCount);
            for (int i = 0; i < size; i++) {
                InputMethodInfo inputMethodInfo = this.mMethodList.get(i);
                printWriterPrinter.println("  InputMethod #" + i + ":");
                inputMethodInfo.dump(printWriterPrinter, "    ");
            }
            printWriterPrinter.println("  Clients:");
            int size2 = this.mClients.size();
            for (int i2 = 0; i2 < size2; i2++) {
                ClientState valueAt = this.mClients.valueAt(i2);
                printWriterPrinter.println("  Client " + valueAt + ":");
                StringBuilder sb = new StringBuilder();
                sb.append("    client=");
                sb.append(valueAt.mClient);
                printWriterPrinter.println(sb.toString());
                printWriterPrinter.println("    fallbackInputConnection=" + valueAt.mFallbackInputConnection);
                printWriterPrinter.println("    sessionRequested=" + valueAt.mSessionRequested);
                printWriterPrinter.println("    sessionRequestedForAccessibility=" + valueAt.mSessionRequestedForAccessibility);
                printWriterPrinter.println("    curSession=" + valueAt.mCurSession);
            }
            printWriterPrinter.println("  mCurMethodId=" + getSelectedMethodIdLocked());
            clientState = this.mCurClient;
            printWriterPrinter.println("  mCurClient=" + clientState + " mCurSeq=" + getSequenceNumberLocked());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("  mCurPerceptible=");
            sb2.append(this.mCurPerceptible);
            printWriterPrinter.println(sb2.toString());
            printWriterPrinter.println("  mCurFocusedWindow=" + this.mCurFocusedWindow + " softInputMode=" + InputMethodDebug.softInputModeToString(this.mCurFocusedWindowSoftInputMode) + " client=" + this.mCurFocusedWindowClient);
            clientState2 = this.mCurFocusedWindowClient;
            printWriterPrinter.println("  mCurId=" + getCurIdLocked() + " mHaveConnection=" + hasConnectionLocked() + " mBoundToMethod=" + this.mBoundToMethod + " mVisibleBound=" + this.mBindingController.isVisibleBound());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("  mCurToken=");
            sb3.append(getCurTokenLocked());
            printWriterPrinter.println(sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append("  mCurTokenDisplayId=");
            sb4.append(this.mCurTokenDisplayId);
            printWriterPrinter.println(sb4.toString());
            printWriterPrinter.println("  mCurHostInputToken=" + this.mCurHostInputToken);
            printWriterPrinter.println("  mCurIntent=" + getCurIntentLocked());
            curMethodLocked = getCurMethodLocked();
            printWriterPrinter.println("  mCurMethod=" + getCurMethodLocked());
            printWriterPrinter.println("  mEnabledSession=" + this.mEnabledSession);
            this.mVisibilityStateComputer.dump(printWriter);
            printWriterPrinter.println("  mInFullscreenMode=" + this.mInFullscreenMode);
            printWriterPrinter.println("  mSystemReady=" + this.mSystemReady + " mInteractive=" + this.mIsInteractive);
            StringBuilder sb5 = new StringBuilder();
            sb5.append("  mSettingsObserver=");
            sb5.append(this.mSettingsObserver);
            printWriterPrinter.println(sb5.toString());
            StringBuilder sb6 = new StringBuilder();
            sb6.append("  mStylusIds=");
            IntArray intArray = this.mStylusIds;
            sb6.append(intArray != null ? Arrays.toString(intArray.toArray()) : "");
            printWriterPrinter.println(sb6.toString());
            printWriterPrinter.println("  mSwitchingController:");
            this.mSwitchingController.dump(printWriterPrinter);
            printWriterPrinter.println("  mSettings:");
            this.mSettings.dumpLocked(printWriterPrinter, "    ");
            printWriterPrinter.println("  mStartInputHistory:");
            this.mStartInputHistory.dump(printWriter, "   ");
            printWriterPrinter.println("  mSoftInputShowHideHistory:");
            this.mSoftInputShowHideHistory.dump(printWriter, "   ");
            printWriterPrinter.println("  mImeTrackerService#History:");
            this.mImeTrackerService.dump(printWriter, "   ");
        }
        if (z) {
            return;
        }
        printWriterPrinter.println(" ");
        if (clientState != null) {
            printWriter.flush();
            try {
                TransferPipe.dumpAsync(clientState.mClient.asBinder(), fileDescriptor, strArr);
            } catch (RemoteException | IOException e) {
                printWriterPrinter.println("Failed to dump input method client: " + e);
            }
        } else {
            printWriterPrinter.println("No input method client.");
        }
        if (clientState2 != null && clientState != clientState2) {
            printWriterPrinter.println(" ");
            printWriterPrinter.println("Warning: Current input method client doesn't match the last focused. window.");
            printWriterPrinter.println("Dumping input method client in the last focused window just in case.");
            printWriterPrinter.println(" ");
            printWriter.flush();
            try {
                TransferPipe.dumpAsync(clientState2.mClient.asBinder(), fileDescriptor, strArr);
            } catch (RemoteException | IOException e2) {
                printWriterPrinter.println("Failed to dump input method client in focused window: " + e2);
            }
        }
        printWriterPrinter.println(" ");
        if (curMethodLocked != null) {
            printWriter.flush();
            try {
                TransferPipe.dumpAsync(curMethodLocked.asBinder(), fileDescriptor, strArr);
                return;
            } catch (RemoteException | IOException e3) {
                printWriterPrinter.println("Failed to dump input method service: " + e3);
                return;
            }
        }
        printWriterPrinter.println("No input method service.");
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) throws RemoteException {
        int callingUid = Binder.getCallingUid();
        if (callingUid != 0 && callingUid != 2000) {
            if (resultReceiver != null) {
                resultReceiver.send(-1, null);
            }
            String str = "InputMethodManagerService does not support shell commands from non-shell users. callingUid=" + callingUid + " args=" + Arrays.toString(strArr);
            if (Process.isCoreUid(callingUid)) {
                Slog.e(this.TAG, str);
                return;
            }
            throw new SecurityException(str);
        }
        new ShellCommandImpl(this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class ShellCommandImpl extends ShellCommand {
        final InputMethodManagerService mService;

        ShellCommandImpl(InputMethodManagerService inputMethodManagerService) {
            this.mService = inputMethodManagerService;
        }

        public int onCommand(String str) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return onCommandWithSystemIdentity(str);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:47:0x009d, code lost:
        
            if (r8.equals("") == false) goto L22;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private int onCommandWithSystemIdentity(String str) {
            boolean z;
            String emptyIfNull = TextUtils.emptyIfNull(str);
            emptyIfNull.hashCode();
            char c = 2;
            switch (emptyIfNull.hashCode()) {
                case -1180406812:
                    if (emptyIfNull.equals("get-last-switch-user-id")) {
                        z = false;
                        break;
                    }
                    z = -1;
                    break;
                case -1067396926:
                    if (emptyIfNull.equals("tracing")) {
                        z = true;
                        break;
                    }
                    z = -1;
                    break;
                case 104385:
                    if (emptyIfNull.equals("ime")) {
                        z = 2;
                        break;
                    }
                    z = -1;
                    break;
                default:
                    z = -1;
                    break;
            }
            switch (z) {
                case false:
                    return this.mService.getLastSwitchUserId(this);
                case true:
                    return this.mService.handleShellCommandTraceInputMethod(this);
                case true:
                    String emptyIfNull2 = TextUtils.emptyIfNull(getNextArg());
                    emptyIfNull2.hashCode();
                    switch (emptyIfNull2.hashCode()) {
                        case -1298848381:
                            if (emptyIfNull2.equals("enable")) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case -1067396926:
                            if (emptyIfNull2.equals("tracing")) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        case 0:
                            break;
                        case 1499:
                            if (emptyIfNull2.equals("-h")) {
                                c = 3;
                                break;
                            }
                            c = 65535;
                            break;
                        case 113762:
                            if (emptyIfNull2.equals("set")) {
                                c = 4;
                                break;
                            }
                            c = 65535;
                            break;
                        case 3198785:
                            if (emptyIfNull2.equals("help")) {
                                c = 5;
                                break;
                            }
                            c = 65535;
                            break;
                        case 3322014:
                            if (emptyIfNull2.equals("list")) {
                                c = 6;
                                break;
                            }
                            c = 65535;
                            break;
                        case 108404047:
                            if (emptyIfNull2.equals("reset")) {
                                c = 7;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1671308008:
                            if (emptyIfNull2.equals("disable")) {
                                c = '\b';
                                break;
                            }
                            c = 65535;
                            break;
                        default:
                            c = 65535;
                            break;
                    }
                    switch (c) {
                        case 0:
                            return this.mService.handleShellCommandEnableDisableInputMethod(this, true);
                        case 1:
                            return this.mService.handleShellCommandTraceInputMethod(this);
                        case 2:
                        case 3:
                        case 5:
                            return onImeCommandHelp();
                        case 4:
                            return this.mService.handleShellCommandSetInputMethod(this);
                        case 6:
                            return this.mService.handleShellCommandListInputMethods(this);
                        case 7:
                            return this.mService.handleShellCommandResetInputMethod(this);
                        case '\b':
                            return this.mService.handleShellCommandEnableDisableInputMethod(this, false);
                        default:
                            getOutPrintWriter().println("Unknown command: " + emptyIfNull2);
                            return -1;
                    }
                default:
                    return handleDefaultCommands(str);
            }
        }

        public void onHelp() {
            PrintWriter outPrintWriter = getOutPrintWriter();
            try {
                outPrintWriter.println("InputMethodManagerService commands:");
                outPrintWriter.println("  help");
                outPrintWriter.println("    Prints this help text.");
                outPrintWriter.println("  dump [options]");
                outPrintWriter.println("    Synonym of dumpsys.");
                outPrintWriter.println("  ime <command> [options]");
                outPrintWriter.println("    Manipulate IMEs.  Run \"ime help\" for details.");
                outPrintWriter.println("  tracing <command>");
                outPrintWriter.println("    start: Start tracing.");
                outPrintWriter.println("    stop : Stop tracing.");
                outPrintWriter.println("    help : Show help.");
                outPrintWriter.close();
            } catch (Throwable th) {
                if (outPrintWriter != null) {
                    try {
                        outPrintWriter.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        private int onImeCommandHelp() {
            IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(getOutPrintWriter(), "  ", 100);
            try {
                indentingPrintWriter.println("ime <command>:");
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.println("list [-a] [-s]");
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.println("prints all enabled input methods.");
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.println("-a: see all input methods");
                indentingPrintWriter.println("-s: only a single summary line of each");
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("enable [--user <USER_ID>] <ID>");
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.println("allows the given input method ID to be used.");
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.print("--user <USER_ID>: Specify which user to enable.");
                indentingPrintWriter.println(" Assumes the current user if not specified.");
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("disable [--user <USER_ID>] <ID>");
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.println("disallows the given input method ID to be used.");
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.print("--user <USER_ID>: Specify which user to disable.");
                indentingPrintWriter.println(" Assumes the current user if not specified.");
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("set [--user <USER_ID>] <ID>");
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.println("switches to the given input method ID.");
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.print("--user <USER_ID>: Specify which user to enable.");
                indentingPrintWriter.println(" Assumes the current user if not specified.");
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("reset [--user <USER_ID>]");
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.println("reset currently selected/enabled IMEs to the default ones as if the device is initially booted with the current locale.");
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.print("--user <USER_ID>: Specify which user to reset.");
                indentingPrintWriter.println(" Assumes the current user if not specified.");
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.close();
                return 0;
            } catch (Throwable th) {
                try {
                    indentingPrintWriter.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getLastSwitchUserId(ShellCommand shellCommand) {
        synchronized (ImfLock.class) {
            shellCommand.getOutPrintWriter().println(this.mLastSwitchUserId);
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00c6 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00d0 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00d3 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0004 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int handleShellCommandListInputMethods(ShellCommand shellCommand) {
        List<InputMethodInfo> enabledInputMethodListLocked;
        int i = -2;
        boolean z = false;
        boolean z2 = false;
        while (true) {
            String nextOption = shellCommand.getNextOption();
            if (nextOption != null) {
                char c = 65535;
                switch (nextOption.hashCode()) {
                    case 1492:
                        if (nextOption.equals("-a")) {
                            c = 0;
                        }
                        switch (c) {
                            case 0:
                                z = true;
                                break;
                            case 1:
                                z2 = true;
                                break;
                            case 2:
                            case 3:
                                i = UserHandle.parseUserArg(shellCommand.getNextArgRequired());
                                break;
                        }
                        break;
                    case 1510:
                        if (nextOption.equals("-s")) {
                            c = 1;
                        }
                        switch (c) {
                        }
                        break;
                    case 1512:
                        if (nextOption.equals("-u")) {
                            c = 2;
                        }
                        switch (c) {
                        }
                        break;
                    case 1333469547:
                        if (nextOption.equals("--user")) {
                            c = 3;
                        }
                        switch (c) {
                        }
                        break;
                    default:
                        switch (c) {
                        }
                        break;
                }
            } else {
                synchronized (ImfLock.class) {
                    int[] resolveUserId = InputMethodUtils.resolveUserId(i, this.mSettings.getCurrentUserId(), shellCommand.getErrPrintWriter());
                    final PrintWriter outPrintWriter = shellCommand.getOutPrintWriter();
                    try {
                        for (int i2 : resolveUserId) {
                            if (z) {
                                enabledInputMethodListLocked = getInputMethodListLocked(i2, 0, 2000);
                            } else {
                                enabledInputMethodListLocked = getEnabledInputMethodListLocked(i2, 2000);
                            }
                            if (resolveUserId.length > 1) {
                                outPrintWriter.print("User #");
                                outPrintWriter.print(i2);
                                outPrintWriter.println(":");
                            }
                            for (InputMethodInfo inputMethodInfo : enabledInputMethodListLocked) {
                                if (z2) {
                                    outPrintWriter.println(inputMethodInfo.getId());
                                } else {
                                    outPrintWriter.print(inputMethodInfo.getId());
                                    outPrintWriter.println(":");
                                    inputMethodInfo.dump(new Printer() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda5
                                        @Override // android.util.Printer
                                        public final void println(String str) {
                                            outPrintWriter.println(str);
                                        }
                                    }, "  ");
                                }
                            }
                        }
                        if (outPrintWriter != null) {
                            outPrintWriter.close();
                        }
                    } finally {
                    }
                }
                return 0;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int handleShellCommandEnableDisableInputMethod(ShellCommand shellCommand, boolean z) {
        boolean z2;
        int handleOptionsForCommandsThatOnlyHaveUserOption = handleOptionsForCommandsThatOnlyHaveUserOption(shellCommand);
        String nextArgRequired = shellCommand.getNextArgRequired();
        PrintWriter outPrintWriter = shellCommand.getOutPrintWriter();
        try {
            PrintWriter errPrintWriter = shellCommand.getErrPrintWriter();
            try {
                synchronized (ImfLock.class) {
                    z2 = false;
                    for (int i : InputMethodUtils.resolveUserId(handleOptionsForCommandsThatOnlyHaveUserOption, this.mSettings.getCurrentUserId(), shellCommand.getErrPrintWriter())) {
                        if (userHasDebugPriv(i, shellCommand)) {
                            z2 |= !handleShellCommandEnableDisableInputMethodInternalLocked(r2, nextArgRequired, z, outPrintWriter, errPrintWriter);
                        }
                    }
                }
                if (errPrintWriter != null) {
                    errPrintWriter.close();
                }
                if (outPrintWriter != null) {
                    outPrintWriter.close();
                }
                return z2 ? -1 : 0;
            } finally {
            }
        } finally {
        }
    }

    private static int handleOptionsForCommandsThatOnlyHaveUserOption(ShellCommand shellCommand) {
        String nextOption;
        do {
            nextOption = shellCommand.getNextOption();
            if (nextOption != null) {
                if (nextOption.equals("-u")) {
                    break;
                }
            } else {
                return -2;
            }
        } while (!nextOption.equals("--user"));
        return UserHandle.parseUserArg(shellCommand.getNextArgRequired());
    }

    @GuardedBy({"ImfLock.class"})
    private boolean handleShellCommandEnableDisableInputMethodInternalLocked(int i, String str, boolean z, PrintWriter printWriter, PrintWriter printWriter2) {
        boolean buildAndPutEnabledInputMethodsStrRemovingIdLocked;
        boolean z2;
        if (i == this.mSettings.getCurrentUserId()) {
            if (!z || this.mMethodMap.containsKey(str)) {
                buildAndPutEnabledInputMethodsStrRemovingIdLocked = setInputMethodEnabledLocked(str, z);
                z2 = false;
            }
            z2 = true;
            buildAndPutEnabledInputMethodsStrRemovingIdLocked = false;
        } else {
            ArrayMap<String, InputMethodInfo> queryMethodMapForUser = queryMethodMapForUser(i);
            InputMethodUtils.InputMethodSettings inputMethodSettings = new InputMethodUtils.InputMethodSettings(this.mContext, queryMethodMapForUser, i, false);
            if (z) {
                if (queryMethodMapForUser.containsKey(str)) {
                    Iterator<InputMethodInfo> it = inputMethodSettings.getEnabledInputMethodListLocked().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            buildAndPutEnabledInputMethodsStrRemovingIdLocked = false;
                            break;
                        }
                        if (TextUtils.equals(it.next().getId(), str)) {
                            buildAndPutEnabledInputMethodsStrRemovingIdLocked = true;
                            break;
                        }
                    }
                    if (!buildAndPutEnabledInputMethodsStrRemovingIdLocked) {
                        inputMethodSettings.appendAndPutEnabledInputMethodLocked(str, false);
                    }
                }
                z2 = true;
                buildAndPutEnabledInputMethodsStrRemovingIdLocked = false;
            } else {
                buildAndPutEnabledInputMethodsStrRemovingIdLocked = inputMethodSettings.buildAndPutEnabledInputMethodsStrRemovingIdLocked(new StringBuilder(), inputMethodSettings.getEnabledInputMethodsAndSubtypeListLocked(), str);
            }
            z2 = false;
        }
        if (z2) {
            printWriter2.print("Unknown input method ");
            printWriter2.print(str);
            printWriter2.println(" cannot be enabled for user #" + i);
            Slog.e(this.TAG, "\"ime enable " + str + "\" for user #" + i + " failed due to its unrecognized IME ID.");
            return false;
        }
        printWriter.print("Input method ");
        printWriter.print(str);
        printWriter.print(": ");
        printWriter.print(z == buildAndPutEnabledInputMethodsStrRemovingIdLocked ? "already " : "now ");
        printWriter.print(z ? ServiceConfigAccessor.PROVIDER_MODE_ENABLED : ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
        printWriter.print(" for user #");
        printWriter.println(i);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int handleShellCommandSetInputMethod(ShellCommand shellCommand) {
        boolean z;
        int handleOptionsForCommandsThatOnlyHaveUserOption = handleOptionsForCommandsThatOnlyHaveUserOption(shellCommand);
        String nextArgRequired = shellCommand.getNextArgRequired();
        PrintWriter outPrintWriter = shellCommand.getOutPrintWriter();
        try {
            PrintWriter errPrintWriter = shellCommand.getErrPrintWriter();
            try {
                synchronized (ImfLock.class) {
                    z = false;
                    for (int i : InputMethodUtils.resolveUserId(handleOptionsForCommandsThatOnlyHaveUserOption, this.mSettings.getCurrentUserId(), shellCommand.getErrPrintWriter())) {
                        if (userHasDebugPriv(i, shellCommand)) {
                            boolean z2 = !switchToInputMethodLocked(nextArgRequired, i);
                            if (z2) {
                                errPrintWriter.print("Unknown input method ");
                                errPrintWriter.print(nextArgRequired);
                                errPrintWriter.print(" cannot be selected for user #");
                                errPrintWriter.println(i);
                                Slog.e(this.TAG, "\"ime set " + nextArgRequired + "\" for user #" + i + " failed due to its unrecognized IME ID.");
                            } else {
                                outPrintWriter.print("Input method ");
                                outPrintWriter.print(nextArgRequired);
                                outPrintWriter.print(" selected for user #");
                                outPrintWriter.println(i);
                            }
                            z |= z2;
                        }
                    }
                }
                if (errPrintWriter != null) {
                    errPrintWriter.close();
                }
                if (outPrintWriter != null) {
                    outPrintWriter.close();
                }
                return z ? -1 : 0;
            } finally {
            }
        } catch (Throwable th) {
            if (outPrintWriter != null) {
                try {
                    outPrintWriter.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int handleShellCommandResetInputMethod(ShellCommand shellCommand) {
        int[] iArr;
        ArrayList<InputMethodInfo> defaultEnabledImes;
        String str;
        int handleOptionsForCommandsThatOnlyHaveUserOption = handleOptionsForCommandsThatOnlyHaveUserOption(shellCommand);
        synchronized (ImfLock.class) {
            final PrintWriter outPrintWriter = shellCommand.getOutPrintWriter();
            try {
                int[] resolveUserId = InputMethodUtils.resolveUserId(handleOptionsForCommandsThatOnlyHaveUserOption, this.mSettings.getCurrentUserId(), shellCommand.getErrPrintWriter());
                int length = resolveUserId.length;
                int i = 0;
                while (i < length) {
                    int i2 = resolveUserId[i];
                    if (userHasDebugPriv(i2, shellCommand)) {
                        if (i2 == this.mSettings.getCurrentUserId()) {
                            hideCurrentInputLocked(this.mCurFocusedWindow, null, 0, null, 15);
                            this.mBindingController.unbindCurrentMethod();
                            ArrayList<InputMethodInfo> enabledInputMethodListLocked = this.mSettings.getEnabledInputMethodListLocked();
                            ArrayList<InputMethodInfo> defaultEnabledImes2 = InputMethodInfoUtils.getDefaultEnabledImes(this.mContext, this.mMethodList);
                            enabledInputMethodListLocked.removeAll(defaultEnabledImes2);
                            Iterator<InputMethodInfo> it = enabledInputMethodListLocked.iterator();
                            while (it.hasNext()) {
                                setInputMethodEnabledLocked(it.next().getId(), false);
                            }
                            Iterator<InputMethodInfo> it2 = defaultEnabledImes2.iterator();
                            while (it2.hasNext()) {
                                setInputMethodEnabledLocked(it2.next().getId(), true);
                            }
                            if (!chooseNewDefaultIMELocked()) {
                                resetSelectedInputMethodAndSubtypeLocked(null);
                            }
                            updateInputMethodsFromSettingsLocked(true);
                            InputMethodUtils.setNonSelectedSystemImesDisabledUntilUsed(getPackageManagerForUser(this.mContext, this.mSettings.getCurrentUserId()), this.mSettings.getEnabledInputMethodListLocked());
                            str = this.mSettings.getSelectedInputMethod();
                            defaultEnabledImes = this.mSettings.getEnabledInputMethodListLocked();
                            iArr = resolveUserId;
                        } else {
                            ArrayMap<String, InputMethodInfo> arrayMap = new ArrayMap<>();
                            ArrayList<InputMethodInfo> arrayList = new ArrayList<>();
                            ArrayMap<String, List<InputMethodSubtype>> arrayMap2 = new ArrayMap<>();
                            AdditionalSubtypeUtils.load(arrayMap2, i2);
                            iArr = resolveUserId;
                            queryInputMethodServicesInternal(this.mContext, i2, arrayMap2, arrayMap, arrayList, 0, this.mSettings.getEnabledInputMethodNames());
                            final InputMethodUtils.InputMethodSettings inputMethodSettings = new InputMethodUtils.InputMethodSettings(this.mContext, arrayMap, i2, false);
                            defaultEnabledImes = InputMethodInfoUtils.getDefaultEnabledImes(this.mContext, arrayList);
                            InputMethodInfo defaultInputMethodByConfig = this.mImmsWrapper.getExtImpl().getDefaultInputMethodByConfig(this.mSettings.getCurrentUserId());
                            if (defaultInputMethodByConfig == null) {
                                defaultInputMethodByConfig = InputMethodInfoUtils.getMostApplicableDefaultIME(defaultEnabledImes);
                            }
                            String id = defaultInputMethodByConfig.getId();
                            inputMethodSettings.putEnabledInputMethodsStr("");
                            defaultEnabledImes.forEach(new Consumer() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda9
                                @Override // java.util.function.Consumer
                                public final void accept(Object obj) {
                                    InputMethodManagerService.lambda$handleShellCommandResetInputMethod$9(InputMethodUtils.InputMethodSettings.this, (InputMethodInfo) obj);
                                }
                            });
                            inputMethodSettings.putSelectedInputMethod(id);
                            inputMethodSettings.putSelectedSubtype(-1);
                            str = id;
                        }
                        outPrintWriter.println("Reset current and enabled IMEs for user #" + i2);
                        outPrintWriter.println("  Selected: " + str);
                        defaultEnabledImes.forEach(new Consumer() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda10
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                InputMethodManagerService.lambda$handleShellCommandResetInputMethod$10(outPrintWriter, (InputMethodInfo) obj);
                            }
                        });
                    } else {
                        iArr = resolveUserId;
                    }
                    i++;
                    resolveUserId = iArr;
                }
                if (outPrintWriter != null) {
                    outPrintWriter.close();
                }
            } finally {
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleShellCommandResetInputMethod$9(InputMethodUtils.InputMethodSettings inputMethodSettings, InputMethodInfo inputMethodInfo) {
        inputMethodSettings.appendAndPutEnabledInputMethodLocked(inputMethodInfo.getId(), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleShellCommandResetInputMethod$10(PrintWriter printWriter, InputMethodInfo inputMethodInfo) {
        printWriter.println("   Enabled: " + inputMethodInfo.getId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x009b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0084 A[Catch: all -> 0x00c3, TRY_LEAVE, TryCatch #1 {all -> 0x00c3, blocks: (B:3:0x0008, B:14:0x0048, B:18:0x006f, B:22:0x007c, B:46:0x0084, B:47:0x0020, B:50:0x002b, B:53:0x0036), top: B:2:0x0008 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int handleShellCommandTraceInputMethod(ShellCommand shellCommand) {
        char c;
        ArrayMap arrayMap;
        String nextArgRequired = shellCommand.getNextArgRequired();
        PrintWriter outPrintWriter = shellCommand.getOutPrintWriter();
        try {
            int hashCode = nextArgRequired.hashCode();
            if (hashCode == -390772652) {
                if (nextArgRequired.equals("save-for-bugreport")) {
                    c = 2;
                    if (c != 0) {
                    }
                    if (outPrintWriter != null) {
                    }
                    boolean isEnabled = ImeTracing.getInstance().isEnabled();
                    synchronized (ImfLock.class) {
                    }
                }
                c = 65535;
                if (c != 0) {
                }
                if (outPrintWriter != null) {
                }
                boolean isEnabled2 = ImeTracing.getInstance().isEnabled();
                synchronized (ImfLock.class) {
                }
            } else if (hashCode != 3540994) {
                if (hashCode == 109757538 && nextArgRequired.equals("start")) {
                    c = 0;
                    if (c != 0) {
                        ImeTracing.getInstance().startTrace(outPrintWriter);
                    } else {
                        if (c != 1) {
                            if (c == 2) {
                                ImeTracing.getInstance().saveForBugreport(outPrintWriter);
                                if (outPrintWriter != null) {
                                    outPrintWriter.close();
                                }
                                return 0;
                            }
                            outPrintWriter.println("Unknown command: " + nextArgRequired);
                            outPrintWriter.println("Input method trace options:");
                            outPrintWriter.println("  start: Start tracing");
                            outPrintWriter.println("  stop: Stop tracing");
                            outPrintWriter.close();
                            return -1;
                        }
                        ImeTracing.getInstance().stopTrace(outPrintWriter);
                    }
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    boolean isEnabled22 = ImeTracing.getInstance().isEnabled();
                    synchronized (ImfLock.class) {
                        arrayMap = new ArrayMap(this.mClients);
                    }
                    for (ClientState clientState : arrayMap.values()) {
                        if (clientState != null) {
                            clientState.mClient.setImeTraceEnabled(isEnabled22);
                        }
                    }
                    return 0;
                }
                c = 65535;
                if (c != 0) {
                }
                if (outPrintWriter != null) {
                }
                boolean isEnabled222 = ImeTracing.getInstance().isEnabled();
                synchronized (ImfLock.class) {
                }
            } else {
                if (nextArgRequired.equals("stop")) {
                    c = 1;
                    if (c != 0) {
                    }
                    if (outPrintWriter != null) {
                    }
                    boolean isEnabled2222 = ImeTracing.getInstance().isEnabled();
                    synchronized (ImfLock.class) {
                    }
                }
                c = 65535;
                if (c != 0) {
                }
                if (outPrintWriter != null) {
                }
                boolean isEnabled22222 = ImeTracing.getInstance().isEnabled();
                synchronized (ImfLock.class) {
                }
            }
        } catch (Throwable th) {
            if (outPrintWriter != null) {
                try {
                    outPrintWriter.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private boolean userHasDebugPriv(int i, ShellCommand shellCommand) {
        if (!this.mUserManagerInternal.hasUserRestriction("no_debugging_features", i)) {
            return true;
        }
        shellCommand.getErrPrintWriter().println("User #" + i + " is restricted with DISALLOW_DEBUGGING_FEATURES.");
        return false;
    }

    public IImeTracker getImeTrackerService() {
        return this.mImeTrackerService;
    }

    private ImeTracker.Token createStatsTokenForFocusedClient(boolean z, int i, int i2) {
        String str;
        ClientState clientState = this.mCurFocusedWindowClient;
        int i3 = clientState != null ? clientState.mUid : -1;
        EditorInfo editorInfo = this.mCurFocusedWindowEditorInfo;
        if (editorInfo != null) {
            str = editorInfo.packageName;
        } else {
            str = "uid(" + i3 + ")";
        }
        if (z) {
            return ImeTracker.forLogging().onRequestShow(str, i3, i, i2);
        }
        return ImeTracker.forLogging().onRequestHide(str, i3, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class InputMethodPrivilegedOperationsImpl extends IInputMethodPrivilegedOperations.Stub {
        private final InputMethodManagerService mImms;
        private final IBinder mToken;

        InputMethodPrivilegedOperationsImpl(InputMethodManagerService inputMethodManagerService, IBinder iBinder) {
            this.mImms = inputMethodManagerService;
            this.mToken = iBinder;
        }

        public void setImeWindowStatusAsync(int i, int i2) {
            this.mImms.setImeWindowStatus(this.mToken, i, i2);
        }

        public void reportStartInputAsync(IBinder iBinder) {
            this.mImms.reportStartInput(this.mToken, iBinder);
        }

        public void createInputContentUriToken(Uri uri, String str, AndroidFuture androidFuture) {
            try {
                androidFuture.complete(this.mImms.createInputContentUriToken(this.mToken, uri, str).asBinder());
            } catch (Throwable th) {
                androidFuture.completeExceptionally(th);
            }
        }

        public void reportFullscreenModeAsync(boolean z) {
            this.mImms.reportFullscreenMode(this.mToken, z);
        }

        public void setInputMethod(String str, AndroidFuture androidFuture) {
            try {
                this.mImms.setInputMethod(this.mToken, str);
                androidFuture.complete((Object) null);
            } catch (Throwable th) {
                androidFuture.completeExceptionally(th);
            }
        }

        public void setInputMethodAndSubtype(String str, InputMethodSubtype inputMethodSubtype, AndroidFuture androidFuture) {
            try {
                this.mImms.setInputMethodAndSubtype(this.mToken, str, inputMethodSubtype);
                androidFuture.complete((Object) null);
            } catch (Throwable th) {
                androidFuture.completeExceptionally(th);
            }
        }

        public void hideMySoftInput(int i, int i2, AndroidFuture androidFuture) {
            try {
                this.mImms.hideMySoftInput(this.mToken, i, i2);
                androidFuture.complete((Object) null);
            } catch (Throwable th) {
                androidFuture.completeExceptionally(th);
            }
        }

        public void showMySoftInput(int i, AndroidFuture androidFuture) {
            try {
                this.mImms.showMySoftInput(this.mToken, i);
                androidFuture.complete((Object) null);
            } catch (Throwable th) {
                androidFuture.completeExceptionally(th);
            }
        }

        public void updateStatusIconAsync(String str, int i) {
            this.mImms.updateStatusIcon(this.mToken, str, i);
        }

        public void switchToPreviousInputMethod(AndroidFuture androidFuture) {
            try {
                androidFuture.complete(Boolean.valueOf(this.mImms.switchToPreviousInputMethod(this.mToken)));
            } catch (Throwable th) {
                androidFuture.completeExceptionally(th);
            }
        }

        public void switchToNextInputMethod(boolean z, AndroidFuture androidFuture) {
            try {
                androidFuture.complete(Boolean.valueOf(this.mImms.switchToNextInputMethod(this.mToken, z)));
            } catch (Throwable th) {
                androidFuture.completeExceptionally(th);
            }
        }

        public void shouldOfferSwitchingToNextInputMethod(AndroidFuture androidFuture) {
            try {
                androidFuture.complete(Boolean.valueOf(this.mImms.shouldOfferSwitchingToNextInputMethod(this.mToken)));
            } catch (Throwable th) {
                androidFuture.completeExceptionally(th);
            }
        }

        public void notifyUserActionAsync() {
            this.mImms.notifyUserAction(this.mToken);
        }

        public void applyImeVisibilityAsync(IBinder iBinder, boolean z, ImeTracker.Token token) {
            this.mImms.applyImeVisibility(this.mToken, iBinder, z, token);
        }

        public void onStylusHandwritingReady(int i, int i2) {
            this.mImms.onStylusHandwritingReady(i, i2);
        }

        public void resetStylusHandwriting(int i) {
            this.mImms.resetStylusHandwriting(i);
        }
    }

    public IInputMethodManagerServiceWrapper getWrapper() {
        return this.mImmsWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class InputMethodManagerServiceWrapper implements IInputMethodManagerServiceWrapper {
        private IInputMethodManagerServiceExt mImmsExt;

        private InputMethodManagerServiceWrapper() {
            this.mImmsExt = (IInputMethodManagerServiceExt) ExtLoader.type(IInputMethodManagerServiceExt.class).base(InputMethodManagerService.this).create();
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public IInputMethodManagerServiceExt getExtImpl() {
            return this.mImmsExt;
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public ClientState getCurClient() {
            return InputMethodManagerService.this.mCurClient;
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public boolean isInputShown() {
            return InputMethodManagerService.this.isInputShown();
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public IBinder getCurTokenLocked() {
            return InputMethodManagerService.this.getCurTokenLocked();
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public boolean chooseNewDefaultIMELocked() {
            return InputMethodManagerService.this.chooseNewDefaultIMELocked();
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public Handler getHandler() {
            return InputMethodManagerService.this.mHandler;
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public void setSelectedMethodIdLocked(String str) {
            if (TextUtils.isEmpty(str)) {
                str = null;
            }
            InputMethodManagerService.this.setSelectedMethodIdLocked(str);
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public void setShowRequested(boolean z) {
            InputMethodManagerService.this.mVisibilityStateComputer.requestImeVisibility(InputMethodManagerService.this.mCurFocusedWindow, z);
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public boolean isShowRequested() {
            return InputMethodManagerService.this.isShowRequestedForCurrentWindow();
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public void setInputShown(boolean z) {
            InputMethodManagerService.this.mVisibilityStateComputer.setInputShown(z);
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public InputMethodBindingController getBindingController() {
            return InputMethodManagerService.this.mBindingController;
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public void setImeWindowStatus(int i, int i2) {
            IBinder curTokenLocked = InputMethodManagerService.this.getCurTokenLocked();
            if (curTokenLocked != null) {
                InputMethodManagerService.this.setImeWindowStatus(curTokenLocked, i, i2);
            }
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public InputMethodMenuController getInputMethodMenuController() {
            return InputMethodManagerService.this.mMenuController;
        }
    }
}
