package com.android.server.trust;

import android.R;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.service.trust.GrantTrustResult;
import android.service.trust.ITrustAgentService;
import android.service.trust.ITrustAgentServiceCallback;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.infra.AndroidFuture;
import com.android.server.trust.TrustAgentWrapper;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@TargetApi(21)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class TrustAgentWrapper {
    private static final String DATA_DURATION = "duration";
    private static final String DATA_ESCROW_TOKEN = "escrow_token";
    private static final String DATA_HANDLE = "handle";
    private static final String DATA_MESSAGE = "message";
    private static final String DATA_USER_ID = "user_id";
    private static final boolean DEBUG = TrustManagerService.DEBUG;
    private static final String EXTRA_COMPONENT_NAME = "componentName";
    private static final int MSG_ADD_ESCROW_TOKEN = 7;
    private static final int MSG_ESCROW_TOKEN_STATE = 9;
    private static final int MSG_GRANT_TRUST = 1;
    private static final int MSG_LOCK_USER = 12;
    private static final int MSG_MANAGING_TRUST = 6;
    private static final int MSG_REMOVE_ESCROW_TOKEN = 8;
    private static final int MSG_RESTART_TIMEOUT = 4;
    private static final int MSG_REVOKE_TRUST = 2;
    private static final int MSG_SET_TRUST_AGENT_FEATURES_COMPLETED = 5;
    private static final int MSG_SHOW_KEYGUARD_ERROR_MESSAGE = 11;
    private static final int MSG_TRUST_TIMEOUT = 3;
    private static final int MSG_UNLOCK_USER = 10;
    private static final String PERMISSION = "android.permission.PROVIDE_TRUST_AGENT";
    private static final long RESTART_TIMEOUT_MILLIS = 300000;
    private static final String TAG = "TrustAgentWrapper";
    private static final String TRUST_EXPIRED_ACTION = "android.server.trust.TRUST_EXPIRED_ACTION";
    private final Intent mAlarmIntent;
    private AlarmManager mAlarmManager;
    private PendingIntent mAlarmPendingIntent;
    private boolean mBound;
    private final BroadcastReceiver mBroadcastReceiver;
    private ITrustAgentServiceCallback mCallback;
    private final ServiceConnection mConnection;
    private final Context mContext;
    private boolean mDisplayTrustGrantedMessage;
    private final Handler mHandler;
    private boolean mManagingTrust;
    private long mMaximumTimeToLock;
    private CharSequence mMessage;
    private final ComponentName mName;
    private long mScheduledRestartUptimeMillis;
    private IBinder mSetTrustAgentFeaturesToken;
    private ITrustAgentService mTrustAgentService;
    private boolean mTrustDisabledByDpm;
    private final TrustManagerService mTrustManagerService;
    private boolean mTrustable;
    private final BroadcastReceiver mTrustableDowngradeReceiver;
    private boolean mTrusted;
    private final int mUserId;
    private boolean mPendingSuccessfulUnlock = false;
    private boolean mWaitingForTrustableDowngrade = false;
    private boolean mWithinSecurityLockdownWindow = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.trust.TrustAgentWrapper$3, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass3 extends Handler {
        AnonymousClass3() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            long j;
            int i = 1;
            boolean z = true;
            boolean z2 = false;
            switch (message.what) {
                case 1:
                    if (!TrustAgentWrapper.this.isConnected()) {
                        Log.w(TrustAgentWrapper.TAG, "Agent is not connected, cannot grant trust: " + TrustAgentWrapper.this.mName.flattenToShortString());
                        return;
                    }
                    TrustAgentWrapper.this.mTrusted = true;
                    TrustAgentWrapper.this.mTrustable = false;
                    Pair pair = (Pair) message.obj;
                    TrustAgentWrapper.this.mMessage = (CharSequence) pair.first;
                    AndroidFuture<GrantTrustResult> androidFuture = (AndroidFuture) pair.second;
                    int i2 = message.arg1;
                    TrustAgentWrapper.this.mDisplayTrustGrantedMessage = (i2 & 8) != 0;
                    if ((i2 & 4) != 0) {
                        TrustAgentWrapper.this.mWaitingForTrustableDowngrade = true;
                        androidFuture.thenAccept(new Consumer() { // from class: com.android.server.trust.TrustAgentWrapper$3$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                TrustAgentWrapper.AnonymousClass3.this.lambda$handleMessage$0((GrantTrustResult) obj);
                            }
                        });
                    } else {
                        TrustAgentWrapper.this.mWaitingForTrustableDowngrade = false;
                    }
                    long j2 = message.getData().getLong(TrustAgentWrapper.DATA_DURATION);
                    if (j2 > 0) {
                        if (TrustAgentWrapper.this.mMaximumTimeToLock != 0) {
                            j = Math.min(j2, TrustAgentWrapper.this.mMaximumTimeToLock);
                            if (TrustAgentWrapper.DEBUG) {
                                Slog.d(TrustAgentWrapper.TAG, "DPM lock timeout in effect. Timeout adjusted from " + j2 + " to " + j);
                            }
                        } else {
                            j = j2;
                        }
                        long elapsedRealtime = SystemClock.elapsedRealtime() + j;
                        TrustAgentWrapper trustAgentWrapper = TrustAgentWrapper.this;
                        trustAgentWrapper.mAlarmPendingIntent = PendingIntent.getBroadcast(trustAgentWrapper.mContext, 0, TrustAgentWrapper.this.mAlarmIntent, 301989888);
                        TrustAgentWrapper.this.mAlarmManager.set(2, elapsedRealtime, TrustAgentWrapper.this.mAlarmPendingIntent);
                    }
                    TrustAgentWrapper.this.mTrustManagerService.mArchive.logGrantTrust(TrustAgentWrapper.this.mUserId, TrustAgentWrapper.this.mName, TrustAgentWrapper.this.mMessage != null ? TrustAgentWrapper.this.mMessage.toString() : null, j2, i2);
                    TrustAgentWrapper.this.mTrustManagerService.updateTrust(TrustAgentWrapper.this.mUserId, i2, androidFuture);
                    return;
                case 2:
                    break;
                case 3:
                    if (TrustAgentWrapper.DEBUG) {
                        Slog.d(TrustAgentWrapper.TAG, "Trust timed out : " + TrustAgentWrapper.this.mName.flattenToShortString());
                    }
                    TrustAgentWrapper.this.mTrustManagerService.mArchive.logTrustTimeout(TrustAgentWrapper.this.mUserId, TrustAgentWrapper.this.mName);
                    TrustAgentWrapper.this.onTrustTimeout();
                    break;
                case 4:
                    Slog.w(TrustAgentWrapper.TAG, "Connection attempt to agent " + TrustAgentWrapper.this.mName.flattenToShortString() + " timed out, rebinding");
                    TrustAgentWrapper.this.destroy();
                    TrustAgentWrapper.this.mTrustManagerService.resetAgent(TrustAgentWrapper.this.mName, TrustAgentWrapper.this.mUserId);
                    return;
                case 5:
                    IBinder iBinder = (IBinder) message.obj;
                    int i3 = message.arg1 == 0 ? 0 : 1;
                    if (TrustAgentWrapper.this.mSetTrustAgentFeaturesToken == iBinder) {
                        TrustAgentWrapper.this.mSetTrustAgentFeaturesToken = null;
                        if (!TrustAgentWrapper.this.mTrustDisabledByDpm || i3 == 0) {
                            return;
                        }
                        if (TrustAgentWrapper.DEBUG) {
                            Slog.d(TrustAgentWrapper.TAG, "Re-enabling agent because it acknowledged enabled features: " + TrustAgentWrapper.this.mName.flattenToShortString());
                        }
                        TrustAgentWrapper.this.mTrustDisabledByDpm = false;
                        TrustAgentWrapper.this.mTrustManagerService.updateTrust(TrustAgentWrapper.this.mUserId, 0);
                        return;
                    }
                    if (TrustAgentWrapper.DEBUG) {
                        Slog.w(TrustAgentWrapper.TAG, "Ignoring MSG_SET_TRUST_AGENT_FEATURES_COMPLETED with obsolete token: " + TrustAgentWrapper.this.mName.flattenToShortString());
                        return;
                    }
                    return;
                case 6:
                    TrustAgentWrapper.this.mManagingTrust = message.arg1 != 0;
                    if (!TrustAgentWrapper.this.mManagingTrust) {
                        TrustAgentWrapper.this.mTrusted = false;
                        TrustAgentWrapper.this.mDisplayTrustGrantedMessage = false;
                        TrustAgentWrapper.this.mMessage = null;
                    }
                    TrustAgentWrapper.this.mTrustManagerService.mArchive.logManagingTrust(TrustAgentWrapper.this.mUserId, TrustAgentWrapper.this.mName, TrustAgentWrapper.this.mManagingTrust);
                    TrustAgentWrapper.this.mTrustManagerService.updateTrust(TrustAgentWrapper.this.mUserId, 0);
                    return;
                case 7:
                    byte[] byteArray = message.getData().getByteArray(TrustAgentWrapper.DATA_ESCROW_TOKEN);
                    int i4 = message.getData().getInt(TrustAgentWrapper.DATA_USER_ID);
                    long addEscrowToken = TrustAgentWrapper.this.mTrustManagerService.addEscrowToken(byteArray, i4);
                    try {
                        if (TrustAgentWrapper.this.mTrustAgentService != null) {
                            TrustAgentWrapper.this.mTrustAgentService.onEscrowTokenAdded(byteArray, addEscrowToken, UserHandle.of(i4));
                        } else {
                            z = false;
                        }
                        z2 = z;
                    } catch (RemoteException e) {
                        TrustAgentWrapper.this.onError(e);
                    }
                    if (z2) {
                        return;
                    }
                    TrustAgentWrapper.this.mTrustManagerService.removeEscrowToken(addEscrowToken, i4);
                    return;
                case 8:
                    long j3 = message.getData().getLong(TrustAgentWrapper.DATA_HANDLE);
                    boolean removeEscrowToken = TrustAgentWrapper.this.mTrustManagerService.removeEscrowToken(j3, message.getData().getInt(TrustAgentWrapper.DATA_USER_ID));
                    try {
                        if (TrustAgentWrapper.this.mTrustAgentService != null) {
                            TrustAgentWrapper.this.mTrustAgentService.onEscrowTokenRemoved(j3, removeEscrowToken);
                            return;
                        }
                        return;
                    } catch (RemoteException e2) {
                        TrustAgentWrapper.this.onError(e2);
                        return;
                    }
                case 9:
                    long j4 = message.getData().getLong(TrustAgentWrapper.DATA_HANDLE);
                    boolean isEscrowTokenActive = TrustAgentWrapper.this.mTrustManagerService.isEscrowTokenActive(j4, message.getData().getInt(TrustAgentWrapper.DATA_USER_ID));
                    try {
                        if (TrustAgentWrapper.this.mTrustAgentService != null) {
                            ITrustAgentService iTrustAgentService = TrustAgentWrapper.this.mTrustAgentService;
                            if (!isEscrowTokenActive) {
                                i = 0;
                            }
                            iTrustAgentService.onTokenStateReceived(j4, i);
                            return;
                        }
                        return;
                    } catch (RemoteException e3) {
                        TrustAgentWrapper.this.onError(e3);
                        return;
                    }
                case 10:
                    TrustAgentWrapper.this.mTrustManagerService.unlockUserWithToken(message.getData().getLong(TrustAgentWrapper.DATA_HANDLE), message.getData().getByteArray(TrustAgentWrapper.DATA_ESCROW_TOKEN), message.getData().getInt(TrustAgentWrapper.DATA_USER_ID));
                    return;
                case 11:
                    TrustAgentWrapper.this.mTrustManagerService.showKeyguardErrorMessage(message.getData().getCharSequence(TrustAgentWrapper.DATA_MESSAGE));
                    return;
                case 12:
                    TrustAgentWrapper.this.mTrusted = false;
                    TrustAgentWrapper.this.mTrustable = false;
                    TrustAgentWrapper.this.mTrustManagerService.updateTrust(TrustAgentWrapper.this.mUserId, 0);
                    TrustAgentWrapper.this.mTrustManagerService.lockUser(TrustAgentWrapper.this.mUserId);
                    return;
                default:
                    return;
            }
            TrustAgentWrapper.this.mTrusted = false;
            TrustAgentWrapper.this.mTrustable = false;
            TrustAgentWrapper.this.mWaitingForTrustableDowngrade = false;
            TrustAgentWrapper.this.mDisplayTrustGrantedMessage = false;
            TrustAgentWrapper.this.mMessage = null;
            TrustAgentWrapper.this.mHandler.removeMessages(3);
            if (message.what == 2) {
                TrustAgentWrapper.this.mTrustManagerService.mArchive.logRevokeTrust(TrustAgentWrapper.this.mUserId, TrustAgentWrapper.this.mName);
            }
            TrustAgentWrapper.this.mTrustManagerService.updateTrust(TrustAgentWrapper.this.mUserId, 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$0(GrantTrustResult grantTrustResult) {
            if (grantTrustResult.getStatus() == 1) {
                TrustAgentWrapper.this.setSecurityWindowTimer();
            }
        }
    }

    public TrustAgentWrapper(Context context, TrustManagerService trustManagerService, Intent intent, UserHandle userHandle) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.trust.TrustAgentWrapper.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent2) {
                if (TrustManagerService.ENABLE_ACTIVE_UNLOCK_FLAG && "android.intent.action.SCREEN_OFF".equals(intent2.getAction())) {
                    TrustAgentWrapper.this.downgradeToTrustable();
                }
            }
        };
        this.mTrustableDowngradeReceiver = broadcastReceiver;
        BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() { // from class: com.android.server.trust.TrustAgentWrapper.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent2) {
                ComponentName componentName = (ComponentName) intent2.getParcelableExtra(TrustAgentWrapper.EXTRA_COMPONENT_NAME, ComponentName.class);
                if (TrustAgentWrapper.TRUST_EXPIRED_ACTION.equals(intent2.getAction()) && TrustAgentWrapper.this.mName.equals(componentName)) {
                    TrustAgentWrapper.this.mHandler.removeMessages(3);
                    TrustAgentWrapper.this.mHandler.sendEmptyMessage(3);
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver2;
        this.mHandler = new AnonymousClass3();
        this.mCallback = new ITrustAgentServiceCallback.Stub() { // from class: com.android.server.trust.TrustAgentWrapper.4
            public void grantTrust(CharSequence charSequence, long j, int i, AndroidFuture androidFuture) {
                if (TrustAgentWrapper.DEBUG) {
                    Slog.d(TrustAgentWrapper.TAG, "enableTrust(" + ((Object) charSequence) + ", durationMs = " + j + ", flags = " + i + ")");
                }
                Message obtainMessage = TrustAgentWrapper.this.mHandler.obtainMessage(1, i, 0, Pair.create(charSequence, androidFuture));
                obtainMessage.getData().putLong(TrustAgentWrapper.DATA_DURATION, j);
                obtainMessage.sendToTarget();
            }

            public void revokeTrust() {
                if (TrustAgentWrapper.DEBUG) {
                    Slog.d(TrustAgentWrapper.TAG, "revokeTrust()");
                }
                TrustAgentWrapper.this.mHandler.sendEmptyMessage(2);
            }

            public void lockUser() {
                TrustAgentWrapper.this.mHandler.sendEmptyMessage(12);
            }

            public void setManagingTrust(boolean z) {
                if (TrustAgentWrapper.DEBUG) {
                    Slog.d(TrustAgentWrapper.TAG, "managingTrust()");
                }
                TrustAgentWrapper.this.mHandler.obtainMessage(6, z ? 1 : 0, 0).sendToTarget();
            }

            public void onConfigureCompleted(boolean z, IBinder iBinder) {
                if (TrustAgentWrapper.DEBUG) {
                    Slog.d(TrustAgentWrapper.TAG, "onSetTrustAgentFeaturesEnabledCompleted(result=" + z);
                }
                TrustAgentWrapper.this.mHandler.obtainMessage(5, z ? 1 : 0, 0, iBinder).sendToTarget();
            }

            public void addEscrowToken(byte[] bArr, int i) {
                if (TrustAgentWrapper.this.mContext.getResources().getBoolean(R.bool.config_allowTheaterModeWakeFromMotion)) {
                    throw new SecurityException("Escrow token API is not allowed.");
                }
                if (TrustAgentWrapper.DEBUG) {
                    Slog.d(TrustAgentWrapper.TAG, "adding escrow token for user " + i);
                }
                Message obtainMessage = TrustAgentWrapper.this.mHandler.obtainMessage(7);
                obtainMessage.getData().putByteArray(TrustAgentWrapper.DATA_ESCROW_TOKEN, bArr);
                obtainMessage.getData().putInt(TrustAgentWrapper.DATA_USER_ID, i);
                obtainMessage.sendToTarget();
            }

            public void isEscrowTokenActive(long j, int i) {
                if (TrustAgentWrapper.this.mContext.getResources().getBoolean(R.bool.config_allowTheaterModeWakeFromMotion)) {
                    throw new SecurityException("Escrow token API is not allowed.");
                }
                if (TrustAgentWrapper.DEBUG) {
                    Slog.d(TrustAgentWrapper.TAG, "checking the state of escrow token on user " + i);
                }
                Message obtainMessage = TrustAgentWrapper.this.mHandler.obtainMessage(9);
                obtainMessage.getData().putLong(TrustAgentWrapper.DATA_HANDLE, j);
                obtainMessage.getData().putInt(TrustAgentWrapper.DATA_USER_ID, i);
                obtainMessage.sendToTarget();
            }

            public void removeEscrowToken(long j, int i) {
                if (TrustAgentWrapper.this.mContext.getResources().getBoolean(R.bool.config_allowTheaterModeWakeFromMotion)) {
                    throw new SecurityException("Escrow token API is not allowed.");
                }
                if (TrustAgentWrapper.DEBUG) {
                    Slog.d(TrustAgentWrapper.TAG, "removing escrow token on user " + i);
                }
                Message obtainMessage = TrustAgentWrapper.this.mHandler.obtainMessage(8);
                obtainMessage.getData().putLong(TrustAgentWrapper.DATA_HANDLE, j);
                obtainMessage.getData().putInt(TrustAgentWrapper.DATA_USER_ID, i);
                obtainMessage.sendToTarget();
            }

            public void unlockUserWithToken(long j, byte[] bArr, int i) {
                if (TrustAgentWrapper.this.mContext.getResources().getBoolean(R.bool.config_allowTheaterModeWakeFromMotion)) {
                    throw new SecurityException("Escrow token API is not allowed.");
                }
                if (TrustAgentWrapper.DEBUG) {
                    Slog.d(TrustAgentWrapper.TAG, "unlocking user " + i);
                }
                Message obtainMessage = TrustAgentWrapper.this.mHandler.obtainMessage(10);
                obtainMessage.getData().putInt(TrustAgentWrapper.DATA_USER_ID, i);
                obtainMessage.getData().putLong(TrustAgentWrapper.DATA_HANDLE, j);
                obtainMessage.getData().putByteArray(TrustAgentWrapper.DATA_ESCROW_TOKEN, bArr);
                obtainMessage.sendToTarget();
            }

            public void showKeyguardErrorMessage(CharSequence charSequence) {
                if (TrustAgentWrapper.DEBUG) {
                    Slog.d(TrustAgentWrapper.TAG, "Showing keyguard error message: " + ((Object) charSequence));
                }
                Message obtainMessage = TrustAgentWrapper.this.mHandler.obtainMessage(11);
                obtainMessage.getData().putCharSequence(TrustAgentWrapper.DATA_MESSAGE, charSequence);
                obtainMessage.sendToTarget();
            }
        };
        ServiceConnection serviceConnection = new ServiceConnection() { // from class: com.android.server.trust.TrustAgentWrapper.5
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (TrustAgentWrapper.DEBUG) {
                    Slog.d(TrustAgentWrapper.TAG, "TrustAgent started : " + componentName.flattenToString());
                }
                TrustAgentWrapper.this.mHandler.removeMessages(4);
                TrustAgentWrapper.this.mTrustAgentService = ITrustAgentService.Stub.asInterface(iBinder);
                TrustAgentWrapper.this.mTrustManagerService.mArchive.logAgentConnected(TrustAgentWrapper.this.mUserId, componentName);
                TrustAgentWrapper trustAgentWrapper = TrustAgentWrapper.this;
                trustAgentWrapper.setCallback(trustAgentWrapper.mCallback);
                TrustAgentWrapper.this.updateDevicePolicyFeatures();
                if (TrustAgentWrapper.this.mPendingSuccessfulUnlock) {
                    TrustAgentWrapper.this.onUnlockAttempt(true);
                    TrustAgentWrapper.this.mPendingSuccessfulUnlock = false;
                }
                if (TrustAgentWrapper.this.mTrustManagerService.isDeviceLockedInner(TrustAgentWrapper.this.mUserId)) {
                    TrustAgentWrapper.this.onDeviceLocked();
                } else {
                    TrustAgentWrapper.this.onDeviceUnlocked();
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                if (TrustAgentWrapper.DEBUG) {
                    Slog.d(TrustAgentWrapper.TAG, "TrustAgent disconnected : " + componentName.flattenToShortString());
                }
                TrustAgentWrapper.this.mTrustAgentService = null;
                TrustAgentWrapper.this.mManagingTrust = false;
                TrustAgentWrapper.this.mSetTrustAgentFeaturesToken = null;
                TrustAgentWrapper.this.mTrustManagerService.mArchive.logAgentDied(TrustAgentWrapper.this.mUserId, componentName);
                TrustAgentWrapper.this.mHandler.sendEmptyMessage(2);
                if (TrustAgentWrapper.this.mBound) {
                    TrustAgentWrapper.this.scheduleRestart();
                }
                if (TrustAgentWrapper.this.mWithinSecurityLockdownWindow) {
                    TrustAgentWrapper.this.mTrustManagerService.lockUser(TrustAgentWrapper.this.mUserId);
                }
            }
        };
        this.mConnection = serviceConnection;
        this.mContext = context;
        this.mTrustManagerService = trustManagerService;
        this.mAlarmManager = (AlarmManager) context.getSystemService("alarm");
        this.mUserId = userHandle.getIdentifier();
        ComponentName component = intent.getComponent();
        this.mName = component;
        Intent putExtra = new Intent(TRUST_EXPIRED_ACTION).putExtra(EXTRA_COMPONENT_NAME, component);
        this.mAlarmIntent = putExtra;
        putExtra.setData(Uri.parse(putExtra.toUri(1)));
        putExtra.setPackage(context.getPackageName());
        IntentFilter intentFilter = new IntentFilter(TRUST_EXPIRED_ACTION);
        intentFilter.addDataScheme(putExtra.getScheme());
        intentFilter.addDataPath(putExtra.toUri(1), 0);
        IntentFilter intentFilter2 = new IntentFilter("android.intent.action.SCREEN_OFF");
        scheduleRestart();
        boolean bindServiceAsUser = context.bindServiceAsUser(intent, serviceConnection, 67108865, userHandle);
        this.mBound = bindServiceAsUser;
        if (bindServiceAsUser) {
            context.registerReceiver(broadcastReceiver2, intentFilter, PERMISSION, null, 2);
            context.registerReceiver(broadcastReceiver, intentFilter2);
        } else {
            Log.e(TAG, "Can't bind to TrustAgent " + component.flattenToShortString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onError(Exception exc) {
        Slog.w(TAG, "Exception ", exc);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTrustTimeout() {
        try {
            ITrustAgentService iTrustAgentService = this.mTrustAgentService;
            if (iTrustAgentService != null) {
                iTrustAgentService.onTrustTimeout();
            }
        } catch (RemoteException e) {
            onError(e);
        }
    }

    public void onUnlockAttempt(boolean z) {
        try {
            ITrustAgentService iTrustAgentService = this.mTrustAgentService;
            if (iTrustAgentService != null) {
                iTrustAgentService.onUnlockAttempt(z);
            } else {
                this.mPendingSuccessfulUnlock = z;
            }
        } catch (RemoteException e) {
            onError(e);
        }
    }

    public void onUserRequestedUnlock(boolean z) {
        try {
            ITrustAgentService iTrustAgentService = this.mTrustAgentService;
            if (iTrustAgentService != null) {
                iTrustAgentService.onUserRequestedUnlock(z);
            }
        } catch (RemoteException e) {
            onError(e);
        }
    }

    public void onUserMayRequestUnlock() {
        try {
            ITrustAgentService iTrustAgentService = this.mTrustAgentService;
            if (iTrustAgentService != null) {
                iTrustAgentService.onUserMayRequestUnlock();
            }
        } catch (RemoteException e) {
            onError(e);
        }
    }

    public void onUnlockLockout(int i) {
        try {
            ITrustAgentService iTrustAgentService = this.mTrustAgentService;
            if (iTrustAgentService != null) {
                iTrustAgentService.onUnlockLockout(i);
            }
        } catch (RemoteException e) {
            onError(e);
        }
    }

    public void onDeviceLocked() {
        this.mWithinSecurityLockdownWindow = false;
        try {
            ITrustAgentService iTrustAgentService = this.mTrustAgentService;
            if (iTrustAgentService != null) {
                iTrustAgentService.onDeviceLocked();
            }
        } catch (RemoteException e) {
            onError(e);
        }
    }

    public void onDeviceUnlocked() {
        try {
            ITrustAgentService iTrustAgentService = this.mTrustAgentService;
            if (iTrustAgentService != null) {
                iTrustAgentService.onDeviceUnlocked();
            }
        } catch (RemoteException e) {
            onError(e);
        }
    }

    public void onEscrowTokenActivated(long j, int i) {
        if (DEBUG) {
            Slog.d(TAG, "onEscrowTokenActivated: " + j + " user: " + i);
        }
        ITrustAgentService iTrustAgentService = this.mTrustAgentService;
        if (iTrustAgentService != null) {
            try {
                iTrustAgentService.onTokenStateReceived(j, 1);
            } catch (RemoteException e) {
                onError(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCallback(ITrustAgentServiceCallback iTrustAgentServiceCallback) {
        try {
            ITrustAgentService iTrustAgentService = this.mTrustAgentService;
            if (iTrustAgentService != null) {
                iTrustAgentService.setCallback(iTrustAgentServiceCallback);
            }
        } catch (RemoteException e) {
            onError(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00c9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean updateDevicePolicyFeatures() {
        boolean z;
        boolean z2 = DEBUG;
        if (z2) {
            Slog.d(TAG, "updateDevicePolicyFeatures(" + this.mName + ")");
        }
        try {
            if (this.mTrustAgentService != null) {
                DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
                if ((devicePolicyManager.getKeyguardDisabledFeatures(null, this.mUserId) & 16) != 0) {
                    List trustAgentConfiguration = devicePolicyManager.getTrustAgentConfiguration(null, this.mName, this.mUserId);
                    z = true;
                    if (z2) {
                        try {
                            Slog.d(TAG, "Detected trust agents disabled. Config = " + trustAgentConfiguration);
                        } catch (RemoteException e) {
                            e = e;
                            onError(e);
                            if (this.mTrustDisabledByDpm != z) {
                            }
                            return z;
                        }
                    }
                    if (trustAgentConfiguration != null && trustAgentConfiguration.size() > 0) {
                        if (z2) {
                            Slog.d(TAG, "TrustAgent " + this.mName.flattenToShortString() + " disabled until it acknowledges " + trustAgentConfiguration);
                        }
                        Binder binder = new Binder();
                        this.mSetTrustAgentFeaturesToken = binder;
                        this.mTrustAgentService.onConfigure(trustAgentConfiguration, binder);
                    }
                } else {
                    this.mTrustAgentService.onConfigure(Collections.EMPTY_LIST, (IBinder) null);
                    z = false;
                }
                long maximumTimeToLock = devicePolicyManager.getMaximumTimeToLock(null, this.mUserId);
                if (maximumTimeToLock != this.mMaximumTimeToLock) {
                    this.mMaximumTimeToLock = maximumTimeToLock;
                    PendingIntent pendingIntent = this.mAlarmPendingIntent;
                    if (pendingIntent != null) {
                        this.mAlarmManager.cancel(pendingIntent);
                        this.mAlarmPendingIntent = null;
                        this.mHandler.sendEmptyMessage(3);
                    }
                }
            } else {
                z = false;
            }
        } catch (RemoteException e2) {
            e = e2;
            z = false;
        }
        if (this.mTrustDisabledByDpm != z) {
            this.mTrustDisabledByDpm = z;
            this.mTrustManagerService.updateTrust(this.mUserId, 0);
        }
        return z;
    }

    public boolean isTrusted() {
        return this.mTrusted && this.mManagingTrust && !this.mTrustDisabledByDpm;
    }

    public boolean isTrustable() {
        return this.mTrustable && this.mManagingTrust && !this.mTrustDisabledByDpm;
    }

    public boolean isTrustableOrWaitingForDowngrade() {
        return this.mWaitingForTrustableDowngrade || isTrustable();
    }

    public void setUntrustable() {
        this.mTrustable = false;
    }

    public void downgradeToTrustable() {
        if (this.mWaitingForTrustableDowngrade) {
            this.mWaitingForTrustableDowngrade = false;
            this.mTrusted = false;
            this.mTrustable = true;
            this.mTrustManagerService.updateTrust(this.mUserId, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSecurityWindowTimer() {
        this.mWithinSecurityLockdownWindow = true;
        this.mAlarmManager.setExact(2, SystemClock.elapsedRealtime() + 15000, TAG, new AlarmManager.OnAlarmListener() { // from class: com.android.server.trust.TrustAgentWrapper.6
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                TrustAgentWrapper.this.mWithinSecurityLockdownWindow = false;
            }
        }, Handler.getMain());
    }

    public boolean isManagingTrust() {
        return this.mManagingTrust && !this.mTrustDisabledByDpm;
    }

    public CharSequence getMessage() {
        return this.mMessage;
    }

    public boolean shouldDisplayTrustGrantedMessage() {
        return this.mDisplayTrustGrantedMessage;
    }

    public void destroy() {
        this.mHandler.removeMessages(4);
        if (this.mBound) {
            if (DEBUG) {
                Slog.d(TAG, "TrustAgent unbound : " + this.mName.flattenToShortString());
            }
            this.mTrustManagerService.mArchive.logAgentStopped(this.mUserId, this.mName);
            this.mContext.unbindService(this.mConnection);
            this.mBound = false;
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
            this.mContext.unregisterReceiver(this.mTrustableDowngradeReceiver);
            this.mTrustAgentService = null;
            this.mSetTrustAgentFeaturesToken = null;
            this.mHandler.sendEmptyMessage(2);
        }
    }

    public boolean isConnected() {
        return this.mTrustAgentService != null;
    }

    public boolean isBound() {
        return this.mBound;
    }

    public long getScheduledRestartUptimeMillis() {
        return this.mScheduledRestartUptimeMillis;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleRestart() {
        this.mHandler.removeMessages(4);
        long uptimeMillis = SystemClock.uptimeMillis() + RESTART_TIMEOUT_MILLIS;
        this.mScheduledRestartUptimeMillis = uptimeMillis;
        this.mHandler.sendEmptyMessageAtTime(4, uptimeMillis);
    }
}
