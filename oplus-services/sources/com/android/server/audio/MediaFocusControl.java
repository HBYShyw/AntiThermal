package com.android.server.audio;

import android.app.AppOpsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusInfo;
import android.media.AudioManager;
import android.media.IAudioFocusDispatcher;
import android.media.MediaMetrics;
import android.media.audiopolicy.IAudioPolicyCallback;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.server.am.ProcessList;
import com.android.server.utils.EventLogger;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class MediaFocusControl implements PlayerFocusEnforcer {
    static final boolean DEBUG;
    static final int DUCKING_IN_APP_SDK_LEVEL = 25;
    static final boolean ENFORCE_DUCKING = true;
    static final boolean ENFORCE_DUCKING_FOR_NEW = true;
    static final boolean ENFORCE_FADEOUT_FOR_FOCUS_LOSS = false;
    static final boolean ENFORCE_MUTING_FOR_RING_OR_CALL = true;
    private static final int MAX_STACK_SIZE = 100;
    private static final int MSG_L_FOCUS_LOSS_AFTER_FADE = 1;
    private static final int MSL_L_FORGET_UID = 2;
    private static final int RING_CALL_MUTING_ENFORCEMENT_DELAY_MS = 100;
    private static final String TAG = "MediaFocusControl";
    private static final int[] USAGES_TO_MUTE_IN_RING_OR_CALL;
    private static final Object mAudioFocusLock;
    private static final EventLogger mEventLogger;
    private static final String mMetricsId = "audio.focus";
    private final AppOpsManager mAppOps;
    private final Context mContext;

    @GuardedBy({"mExtFocusChangeLock"})
    private long mExtFocusChangeCounter;
    private PlayerFocusEnforcer mFocusEnforcer;
    private Handler mFocusHandler;
    private HandlerThread mFocusThread;
    private boolean mMultiAudioFocusEnabled;
    private boolean mRingOrCallActive = false;
    private final Object mExtFocusChangeLock = new Object();
    private final Stack<FocusRequester> mFocusStack = new Stack<>();
    ArrayList<FocusRequester> mMultiAudioFocusList = new ArrayList<>();
    private boolean mNotifyFocusOwnerOnDuck = true;
    private ArrayList<IAudioPolicyCallback> mFocusFollowers = new ArrayList<>();

    @GuardedBy({"mAudioFocusLock"})
    private IAudioPolicyCallback mFocusPolicy = null;

    @GuardedBy({"mAudioFocusLock"})
    private IAudioPolicyCallback mPreviousFocusPolicy = null;
    private HashMap<String, FocusRequester> mFocusOwnersForFocusPolicy = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getFadeOutDurationOnFocusLossMillis(AudioAttributes audioAttributes) {
        return 0L;
    }

    static {
        String str = Build.TYPE;
        DEBUG = "eng".equals(str) || "userdebug".equals(str);
        mAudioFocusLock = new Object();
        mEventLogger = new EventLogger(50, "focus commands as seen by MediaFocusControl");
        USAGES_TO_MUTE_IN_RING_OR_CALL = new int[]{1, 14};
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MediaFocusControl(Context context, PlayerFocusEnforcer playerFocusEnforcer) {
        this.mMultiAudioFocusEnabled = false;
        this.mContext = context;
        this.mAppOps = (AppOpsManager) context.getSystemService("appops");
        this.mFocusEnforcer = playerFocusEnforcer;
        ContentResolver contentResolver = context.getContentResolver();
        this.mMultiAudioFocusEnabled = Settings.System.getIntForUser(contentResolver, "multi_audio_focus_enabled", 0, contentResolver.getUserId()) != 0;
        initFocusThreading();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dump(PrintWriter printWriter) {
        printWriter.println("\nMediaFocusControl dump time: " + DateFormat.getTimeInstance().format(new Date()));
        dumpFocusStack(printWriter);
        printWriter.println("\n");
        mEventLogger.dump(printWriter);
        dumpMultiAudioFocus(printWriter);
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public boolean duckPlayers(FocusRequester focusRequester, FocusRequester focusRequester2, boolean z) {
        return this.mFocusEnforcer.duckPlayers(focusRequester, focusRequester2, z);
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public void restoreVShapedPlayers(FocusRequester focusRequester) {
        this.mFocusEnforcer.restoreVShapedPlayers(focusRequester);
        this.mFocusHandler.removeEqualMessages(2, new ForgetFadeUidInfo(focusRequester.getClientUid()));
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public void mutePlayersForCall(int[] iArr) {
        this.mFocusEnforcer.mutePlayersForCall(iArr);
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public void unmutePlayersForCall() {
        this.mFocusEnforcer.unmutePlayersForCall();
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public boolean fadeOutPlayers(FocusRequester focusRequester, FocusRequester focusRequester2) {
        return this.mFocusEnforcer.fadeOutPlayers(focusRequester, focusRequester2);
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public void forgetUid(int i) {
        this.mFocusEnforcer.forgetUid(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void noFocusForSuspendedApp(String str, int i) {
        synchronized (mAudioFocusLock) {
            Iterator<FocusRequester> it = this.mFocusStack.iterator();
            ArrayList arrayList = new ArrayList();
            while (it.hasNext()) {
                FocusRequester next = it.next();
                if (next.hasSameUid(i) && next.hasSamePackage(str)) {
                    arrayList.add(next.getClientId());
                    mEventLogger.enqueue(new EventLogger.StringEvent("focus owner:" + next.getClientId() + " in uid:" + i + " pack: " + str + " getting AUDIOFOCUS_LOSS due to app suspension").printLog(TAG));
                    next.dispatchFocusChange(-1);
                }
            }
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                removeFocusStackEntry((String) it2.next(), false, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasAudioFocusUsers() {
        boolean z;
        synchronized (mAudioFocusLock) {
            z = !this.mFocusStack.empty();
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void discardAudioFocusOwner() {
        synchronized (mAudioFocusLock) {
            if (!this.mFocusStack.empty()) {
                FocusRequester pop = this.mFocusStack.pop();
                pop.handleFocusLoss(-1, null, false);
                pop.release();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<AudioFocusInfo> getFocusStack() {
        ArrayList arrayList;
        synchronized (mAudioFocusLock) {
            arrayList = new ArrayList(this.mFocusStack.size());
            Iterator<FocusRequester> it = this.mFocusStack.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().toAudioFocusInfo());
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean sendFocusLoss(AudioFocusInfo audioFocusInfo) {
        FocusRequester focusRequester;
        synchronized (mAudioFocusLock) {
            Iterator<FocusRequester> it = this.mFocusStack.iterator();
            while (true) {
                focusRequester = null;
                if (!it.hasNext()) {
                    break;
                }
                FocusRequester next = it.next();
                if (next.getClientId().equals(audioFocusInfo.getClientId())) {
                    next.handleFocusLoss(-1, null, false);
                    focusRequester = next;
                    break;
                }
            }
            if (focusRequester == null) {
                return false;
            }
            this.mFocusStack.remove(focusRequester);
            focusRequester.release();
            return true;
        }
    }

    @GuardedBy({"mAudioFocusLock"})
    private void notifyTopOfAudioFocusStack() {
        if (!this.mFocusStack.empty() && canReassignAudioFocus()) {
            this.mFocusStack.peek().handleFocusGain(1);
        }
        if (!this.mMultiAudioFocusEnabled || this.mMultiAudioFocusList.isEmpty()) {
            return;
        }
        Iterator<FocusRequester> it = this.mMultiAudioFocusList.iterator();
        while (it.hasNext()) {
            FocusRequester next = it.next();
            if (isLockedFocusOwner(next)) {
                next.handleFocusGain(1);
            }
        }
    }

    @GuardedBy({"mAudioFocusLock"})
    private void propagateFocusLossFromGain_syncAf(int i, FocusRequester focusRequester, boolean z) {
        LinkedList linkedList = new LinkedList();
        if (!this.mFocusStack.empty()) {
            Iterator<FocusRequester> it = this.mFocusStack.iterator();
            while (it.hasNext()) {
                FocusRequester next = it.next();
                if (next.handleFocusLossFromGain(i, focusRequester, z)) {
                    linkedList.add(next.getClientId());
                }
            }
        }
        if (this.mMultiAudioFocusEnabled && !this.mMultiAudioFocusList.isEmpty()) {
            Iterator<FocusRequester> it2 = this.mMultiAudioFocusList.iterator();
            while (it2.hasNext()) {
                FocusRequester next2 = it2.next();
                if (next2.handleFocusLossFromGain(i, focusRequester, z)) {
                    linkedList.add(next2.getClientId());
                }
            }
        }
        Iterator it3 = linkedList.iterator();
        while (it3.hasNext()) {
            removeFocusStackEntry((String) it3.next(), false, true);
        }
    }

    private void dumpFocusStack(PrintWriter printWriter) {
        printWriter.println("\nAudio Focus stack entries (last is top of stack):");
        synchronized (mAudioFocusLock) {
            Iterator<FocusRequester> it = this.mFocusStack.iterator();
            while (it.hasNext()) {
                it.next().dump(printWriter);
            }
            printWriter.println("\n");
            if (this.mFocusPolicy == null) {
                printWriter.println("No external focus policy\n");
            } else {
                printWriter.println("External focus policy: " + this.mFocusPolicy + ", focus owners:\n");
                dumpExtFocusPolicyFocusOwners(printWriter);
            }
        }
        printWriter.println("\n");
        printWriter.println(" Notify on duck:  " + this.mNotifyFocusOwnerOnDuck + "\n");
        printWriter.println(" In ring or call: " + this.mRingOrCallActive + "\n");
    }

    @GuardedBy({"mAudioFocusLock"})
    private void removeFocusStackEntry(String str, boolean z, boolean z2) {
        if (!this.mFocusStack.empty() && this.mFocusStack.peek().hasSameClient(str)) {
            FocusRequester pop = this.mFocusStack.pop();
            pop.maybeRelease();
            r1 = z2 ? pop.toAudioFocusInfo() : null;
            if (z) {
                notifyTopOfAudioFocusStack();
            }
        } else {
            Iterator<FocusRequester> it = this.mFocusStack.iterator();
            while (it.hasNext()) {
                FocusRequester next = it.next();
                if (next.hasSameClient(str)) {
                    Log.i(TAG, "AudioFocus  removeFocusStackEntry(): removing entry for " + str);
                    it.remove();
                    if (z2) {
                        r1 = next.toAudioFocusInfo();
                    }
                    next.maybeRelease();
                }
            }
        }
        if (r1 != null) {
            r1.clearLossReceived();
            notifyExtPolicyFocusLoss_syncAf(r1, false);
        }
        if (!this.mMultiAudioFocusEnabled || this.mMultiAudioFocusList.isEmpty()) {
            return;
        }
        Iterator<FocusRequester> it2 = this.mMultiAudioFocusList.iterator();
        while (it2.hasNext()) {
            FocusRequester next2 = it2.next();
            if (next2.hasSameClient(str)) {
                it2.remove();
                next2.release();
            }
        }
        if (z) {
            notifyTopOfAudioFocusStack();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mAudioFocusLock"})
    public void removeFocusStackEntryOnDeath(IBinder iBinder) {
        boolean z = !this.mFocusStack.isEmpty() && this.mFocusStack.peek().hasSameBinder(iBinder);
        Iterator<FocusRequester> it = this.mFocusStack.iterator();
        while (it.hasNext()) {
            FocusRequester next = it.next();
            if (next.hasSameBinder(iBinder)) {
                Log.i(TAG, "AudioFocus  removeFocusStackEntryOnDeath(): removing entry for " + iBinder);
                mEventLogger.enqueue(new EventLogger.StringEvent("focus requester:" + next.getClientId() + " in uid:" + next.getClientUid() + " pack:" + next.getPackageName() + " died"));
                notifyExtPolicyFocusLoss_syncAf(next.toAudioFocusInfo(), false);
                it.remove();
                next.release();
            }
        }
        if (z) {
            notifyTopOfAudioFocusStack();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mAudioFocusLock"})
    public void removeFocusEntryForExtPolicyOnDeath(IBinder iBinder) {
        if (this.mFocusOwnersForFocusPolicy.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<String, FocusRequester>> it = this.mFocusOwnersForFocusPolicy.entrySet().iterator();
        while (it.hasNext()) {
            FocusRequester value = it.next().getValue();
            if (value.hasSameBinder(iBinder)) {
                it.remove();
                mEventLogger.enqueue(new EventLogger.StringEvent("focus requester:" + value.getClientId() + " in uid:" + value.getClientUid() + " pack:" + value.getPackageName() + " died"));
                value.release();
                notifyExtFocusPolicyFocusAbandon_syncAf(value.toAudioFocusInfo());
                return;
            }
        }
    }

    private boolean canReassignAudioFocus() {
        return this.mFocusStack.isEmpty() || !isLockedFocusOwner(this.mFocusStack.peek());
    }

    private boolean isLockedFocusOwner(FocusRequester focusRequester) {
        return focusRequester.hasSameClient("AudioFocus_For_Phone_Ring_And_Calls") || focusRequester.isLockedFocusOwner();
    }

    @GuardedBy({"mAudioFocusLock"})
    private int pushBelowLockedFocusOwnersAndPropagate(FocusRequester focusRequester) {
        if (DEBUG) {
            Log.v(TAG, "pushBelowLockedFocusOwnersAndPropagate client=" + focusRequester.getClientId());
        }
        int size = this.mFocusStack.size();
        for (int size2 = this.mFocusStack.size() - 1; size2 >= 0; size2--) {
            if (isLockedFocusOwner(this.mFocusStack.elementAt(size2))) {
                size = size2;
            }
        }
        if (size == this.mFocusStack.size()) {
            Log.e(TAG, "No exclusive focus owner found in propagateFocusLossFromGain_syncAf()", new Exception());
            propagateFocusLossFromGain_syncAf(focusRequester.getGainRequest(), focusRequester, false);
            this.mFocusStack.push(focusRequester);
            return 1;
        }
        if (DEBUG) {
            Log.v(TAG, "> lastLockedFocusOwnerIndex=" + size);
        }
        this.mFocusStack.insertElementAt(focusRequester, size);
        LinkedList<String> linkedList = new LinkedList();
        for (int i = size - 1; i >= 0; i--) {
            if (this.mFocusStack.elementAt(i).handleFocusLossFromGain(focusRequester.getGainRequest(), focusRequester, false)) {
                linkedList.add(this.mFocusStack.elementAt(i).getClientId());
            }
        }
        for (String str : linkedList) {
            if (DEBUG) {
                Log.v(TAG, "> removing focus client " + str);
            }
            removeFocusStackEntry(str, false, true);
        }
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AudioFocusDeathHandler implements IBinder.DeathRecipient {
        private IBinder mCb;

        AudioFocusDeathHandler(IBinder iBinder) {
            this.mCb = iBinder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (MediaFocusControl.mAudioFocusLock) {
                if (MediaFocusControl.this.mFocusPolicy != null) {
                    MediaFocusControl.this.removeFocusEntryForExtPolicyOnDeath(this.mCb);
                } else {
                    MediaFocusControl.this.removeFocusStackEntryOnDeath(this.mCb);
                    if (MediaFocusControl.this.mMultiAudioFocusEnabled && !MediaFocusControl.this.mMultiAudioFocusList.isEmpty()) {
                        Iterator<FocusRequester> it = MediaFocusControl.this.mMultiAudioFocusList.iterator();
                        while (it.hasNext()) {
                            FocusRequester next = it.next();
                            if (next.hasSameBinder(this.mCb)) {
                                it.remove();
                                next.release();
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setDuckingInExtPolicyAvailable(boolean z) {
        this.mNotifyFocusOwnerOnDuck = !z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean mustNotifyFocusOwnerOnDuck() {
        return this.mNotifyFocusOwnerOnDuck;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addFocusFollower(IAudioPolicyCallback iAudioPolicyCallback) {
        boolean z;
        if (iAudioPolicyCallback == null) {
            return;
        }
        synchronized (mAudioFocusLock) {
            Iterator<IAudioPolicyCallback> it = this.mFocusFollowers.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                } else if (it.next().asBinder().equals(iAudioPolicyCallback.asBinder())) {
                    z = true;
                    break;
                }
            }
            if (z) {
                return;
            }
            this.mFocusFollowers.add(iAudioPolicyCallback);
            notifyExtPolicyCurrentFocusAsync(iAudioPolicyCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeFocusFollower(IAudioPolicyCallback iAudioPolicyCallback) {
        if (iAudioPolicyCallback == null) {
            return;
        }
        synchronized (mAudioFocusLock) {
            Iterator<IAudioPolicyCallback> it = this.mFocusFollowers.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                IAudioPolicyCallback next = it.next();
                if (next.asBinder().equals(iAudioPolicyCallback.asBinder())) {
                    this.mFocusFollowers.remove(next);
                    break;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFocusPolicy(IAudioPolicyCallback iAudioPolicyCallback, boolean z) {
        if (iAudioPolicyCallback == null) {
            return;
        }
        synchronized (mAudioFocusLock) {
            if (z) {
                this.mPreviousFocusPolicy = this.mFocusPolicy;
            }
            this.mFocusPolicy = iAudioPolicyCallback;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unsetFocusPolicy(IAudioPolicyCallback iAudioPolicyCallback, boolean z) {
        if (iAudioPolicyCallback == null) {
            return;
        }
        synchronized (mAudioFocusLock) {
            if (this.mFocusPolicy == iAudioPolicyCallback) {
                if (z) {
                    this.mFocusPolicy = this.mPreviousFocusPolicy;
                } else {
                    this.mFocusPolicy = null;
                }
            }
        }
    }

    void notifyExtPolicyCurrentFocusAsync(final IAudioPolicyCallback iAudioPolicyCallback) {
        new Thread() { // from class: com.android.server.audio.MediaFocusControl.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                synchronized (MediaFocusControl.mAudioFocusLock) {
                    if (MediaFocusControl.this.mFocusStack.isEmpty()) {
                        return;
                    }
                    try {
                        iAudioPolicyCallback.notifyAudioFocusGrant(((FocusRequester) MediaFocusControl.this.mFocusStack.peek()).toAudioFocusInfo(), 1);
                    } catch (RemoteException e) {
                        Log.e(MediaFocusControl.TAG, "Can't call notifyAudioFocusGrant() on IAudioPolicyCallback " + iAudioPolicyCallback.asBinder(), e);
                    }
                }
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyExtPolicyFocusGrant_syncAf(AudioFocusInfo audioFocusInfo, int i) {
        Iterator<IAudioPolicyCallback> it = this.mFocusFollowers.iterator();
        while (it.hasNext()) {
            IAudioPolicyCallback next = it.next();
            try {
                next.notifyAudioFocusGrant(audioFocusInfo, i);
            } catch (RemoteException e) {
                Log.e(TAG, "Can't call notifyAudioFocusGrant() on IAudioPolicyCallback " + next.asBinder(), e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyExtPolicyFocusLoss_syncAf(AudioFocusInfo audioFocusInfo, boolean z) {
        Iterator<IAudioPolicyCallback> it = this.mFocusFollowers.iterator();
        while (it.hasNext()) {
            IAudioPolicyCallback next = it.next();
            try {
                next.notifyAudioFocusLoss(audioFocusInfo, z);
            } catch (RemoteException e) {
                Log.e(TAG, "Can't call notifyAudioFocusLoss() on IAudioPolicyCallback " + next.asBinder(), e);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0054  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    boolean notifyExtFocusPolicyFocusRequest_syncAf(AudioFocusInfo audioFocusInfo, IAudioFocusDispatcher iAudioFocusDispatcher, IBinder iBinder) {
        boolean z;
        if (DEBUG) {
            Log.v(TAG, "notifyExtFocusPolicyFocusRequest client=" + audioFocusInfo.getClientId() + " dispatcher=" + iAudioFocusDispatcher);
        }
        synchronized (this.mExtFocusChangeLock) {
            long j = this.mExtFocusChangeCounter;
            this.mExtFocusChangeCounter = 1 + j;
            audioFocusInfo.setGen(j);
        }
        FocusRequester focusRequester = this.mFocusOwnersForFocusPolicy.get(audioFocusInfo.getClientId());
        try {
            if (focusRequester != null) {
                if (!focusRequester.hasSameDispatcher(iAudioFocusDispatcher)) {
                    focusRequester.release();
                } else {
                    z = false;
                    if (z) {
                        AudioFocusDeathHandler audioFocusDeathHandler = new AudioFocusDeathHandler(iBinder);
                        try {
                            iBinder.linkToDeath(audioFocusDeathHandler, 0);
                            this.mFocusOwnersForFocusPolicy.put(audioFocusInfo.getClientId(), new FocusRequester(audioFocusInfo, iAudioFocusDispatcher, iBinder, audioFocusDeathHandler, this));
                        } catch (RemoteException unused) {
                            return false;
                        }
                    }
                    this.mFocusPolicy.notifyAudioFocusRequest(audioFocusInfo, 1);
                    return true;
                }
            }
            this.mFocusPolicy.notifyAudioFocusRequest(audioFocusInfo, 1);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "Can't call notifyAudioFocusRequest() on IAudioPolicyCallback " + this.mFocusPolicy.asBinder(), e);
            return false;
        }
        z = true;
        if (z) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFocusRequestResultFromExtPolicy(AudioFocusInfo audioFocusInfo, int i) {
        FocusRequester focusRequester;
        synchronized (this.mExtFocusChangeLock) {
            if (audioFocusInfo.getGen() > this.mExtFocusChangeCounter) {
                return;
            }
            if (i == 0) {
                focusRequester = this.mFocusOwnersForFocusPolicy.remove(audioFocusInfo.getClientId());
            } else {
                focusRequester = this.mFocusOwnersForFocusPolicy.get(audioFocusInfo.getClientId());
            }
            if (focusRequester != null) {
                focusRequester.dispatchFocusResultFromExtPolicy(i);
            }
        }
    }

    boolean notifyExtFocusPolicyFocusAbandon_syncAf(AudioFocusInfo audioFocusInfo) {
        if (this.mFocusPolicy == null) {
            return false;
        }
        FocusRequester remove = this.mFocusOwnersForFocusPolicy.remove(audioFocusInfo.getClientId());
        if (remove != null) {
            remove.release();
        }
        try {
            this.mFocusPolicy.notifyAudioFocusAbandon(audioFocusInfo);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "Can't call notifyAudioFocusAbandon() on IAudioPolicyCallback " + this.mFocusPolicy.asBinder(), e);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int dispatchFocusChange(AudioFocusInfo audioFocusInfo, int i) {
        FocusRequester focusRequester;
        boolean z = DEBUG;
        if (z) {
            Log.v(TAG, "dispatchFocusChange " + i + " to afi client=" + audioFocusInfo.getClientId());
        }
        synchronized (mAudioFocusLock) {
            if (this.mFocusPolicy == null) {
                if (z) {
                    Log.v(TAG, "> failed: no focus policy");
                }
                return 0;
            }
            if (i == -1) {
                focusRequester = this.mFocusOwnersForFocusPolicy.remove(audioFocusInfo.getClientId());
            } else {
                focusRequester = this.mFocusOwnersForFocusPolicy.get(audioFocusInfo.getClientId());
            }
            if (focusRequester != null) {
                return focusRequester.dispatchFocusChange(i);
            }
            if (z) {
                Log.v(TAG, "> failed: no such focus requester known");
            }
            return 0;
        }
    }

    private void dumpExtFocusPolicyFocusOwners(PrintWriter printWriter) {
        Iterator<Map.Entry<String, FocusRequester>> it = this.mFocusOwnersForFocusPolicy.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().dump(printWriter);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getCurrentAudioFocus() {
        synchronized (mAudioFocusLock) {
            if (this.mFocusStack.empty()) {
                return 0;
            }
            return this.mFocusStack.peek().getGainRequest();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int getFocusRampTimeMs(int i, AudioAttributes audioAttributes) {
        int usage = audioAttributes.getUsage();
        if (usage == 16) {
            return ProcessList.PREVIOUS_APP_ADJ;
        }
        if (usage == 1002) {
            return 500;
        }
        if (usage == 1003) {
            return ProcessList.PREVIOUS_APP_ADJ;
        }
        switch (usage) {
            case 1:
            case 14:
                return 1000;
            case 2:
            case 3:
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
            case 13:
                return 500;
            case 4:
            case 6:
            case 11:
            case 12:
                return ProcessList.PREVIOUS_APP_ADJ;
            default:
                return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r15v1 */
    /* JADX WARN: Type inference failed for: r15v2, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r15v4 */
    public int requestAudioFocus(AudioAttributes audioAttributes, int i, IBinder iBinder, IAudioFocusDispatcher iAudioFocusDispatcher, String str, String str2, String str3, int i2, int i3, boolean z, int i4) {
        int i5;
        ?? r15;
        int i6;
        AudioFocusInfo audioFocusInfo;
        boolean z2;
        boolean z3;
        byte b;
        new MediaMetrics.Item(mMetricsId).setUid(Binder.getCallingUid()).set(MediaMetrics.Property.CALLING_PACKAGE, str2).set(MediaMetrics.Property.CLIENT_NAME, str).set(MediaMetrics.Property.EVENT, "requestAudioFocus").set(MediaMetrics.Property.FLAGS, Integer.valueOf(i2)).set(MediaMetrics.Property.FOCUS_CHANGE_HINT, AudioManager.audioFocusToString(i)).record();
        int callingUid = i2 == 8 ? i4 : Binder.getCallingUid();
        mEventLogger.enqueue(new EventLogger.StringEvent("requestAudioFocus() from uid/pid " + callingUid + "/" + Binder.getCallingPid() + " AA=" + audioAttributes.usageToString() + "/" + audioAttributes.contentTypeToString() + " clientId=" + str + " callingPack=" + str2 + " req=" + i + " flags=0x" + Integer.toHexString(i2) + " sdk=" + i3).printLog(TAG));
        if (!iBinder.pingBinder()) {
            Log.e(TAG, " AudioFocus DOA client for requestAudioFocus(), aborting.");
            return 0;
        }
        if (i2 != 8 && this.mAppOps.noteOp(32, Binder.getCallingUid(), str2, str3, (String) null) != 0) {
            return 0;
        }
        synchronized (mAudioFocusLock) {
            if (this.mFocusStack.size() > 100) {
                Log.e(TAG, "Max AudioFocus stack size reached, failing requestAudioFocus()");
                return 0;
            }
            int i7 = (!this.mRingOrCallActive) & ("AudioFocus_For_Phone_Ring_And_Calls".compareTo(str) == 0);
            if (i7 != false) {
                this.mRingOrCallActive = true;
            }
            if (this.mFocusPolicy != null) {
                i5 = 100;
                r15 = 0;
                i6 = callingUid;
                audioFocusInfo = new AudioFocusInfo(audioAttributes, callingUid, str, str2, i, 0, i2, i3);
            } else {
                i5 = 100;
                r15 = 0;
                i6 = callingUid;
                audioFocusInfo = null;
            }
            AudioFocusInfo audioFocusInfo2 = audioFocusInfo;
            if (canReassignAudioFocus()) {
                z2 = r15;
            } else {
                if ((i2 & 1) == 0) {
                    return r15;
                }
                z2 = true;
            }
            if (this.mFocusPolicy != null) {
                return notifyExtFocusPolicyFocusRequest_syncAf(audioFocusInfo2, iAudioFocusDispatcher, iBinder) ? i5 : r15;
            }
            AudioFocusDeathHandler audioFocusDeathHandler = new AudioFocusDeathHandler(iBinder);
            try {
                iBinder.linkToDeath(audioFocusDeathHandler, r15);
                if (this.mFocusStack.empty() || !this.mFocusStack.peek().hasSameClient(str)) {
                    z3 = true;
                } else {
                    FocusRequester peek = this.mFocusStack.peek();
                    if (peek.getGainRequest() == i && peek.getGrantFlags() == i2) {
                        iBinder.unlinkToDeath(audioFocusDeathHandler, r15);
                        notifyExtPolicyFocusGrant_syncAf(peek.toAudioFocusInfo(), 1);
                        return 1;
                    }
                    z3 = true;
                    if (!z2) {
                        this.mFocusStack.pop();
                        peek.release();
                    }
                }
                removeFocusStackEntry(str, r15, r15);
                boolean z4 = z3;
                FocusRequester focusRequester = new FocusRequester(audioAttributes, i, i2, iAudioFocusDispatcher, iBinder, str, audioFocusDeathHandler, str2, i6, this, i3);
                if (this.mMultiAudioFocusEnabled && i == z4) {
                    if (i7 != false) {
                        if (!this.mMultiAudioFocusList.isEmpty()) {
                            Iterator<FocusRequester> it = this.mMultiAudioFocusList.iterator();
                            while (it.hasNext()) {
                                it.next().handleFocusLossFromGain(i, focusRequester, z);
                            }
                        }
                    } else {
                        if (!this.mMultiAudioFocusList.isEmpty()) {
                            Iterator<FocusRequester> it2 = this.mMultiAudioFocusList.iterator();
                            while (it2.hasNext()) {
                                if (it2.next().getClientUid() == Binder.getCallingUid()) {
                                    b = false;
                                    break;
                                }
                            }
                        }
                        b = z4 ? 1 : 0;
                        if (b != false) {
                            this.mMultiAudioFocusList.add(focusRequester);
                        }
                        focusRequester.handleFocusGainFromRequest(z4 ? 1 : 0);
                        notifyExtPolicyFocusGrant_syncAf(focusRequester.toAudioFocusInfo(), z4 ? 1 : 0);
                        return z4 ? 1 : 0;
                    }
                }
                if (z2) {
                    int pushBelowLockedFocusOwnersAndPropagate = pushBelowLockedFocusOwnersAndPropagate(focusRequester);
                    if (pushBelowLockedFocusOwnersAndPropagate != 0) {
                        notifyExtPolicyFocusGrant_syncAf(focusRequester.toAudioFocusInfo(), pushBelowLockedFocusOwnersAndPropagate);
                    }
                    return pushBelowLockedFocusOwnersAndPropagate;
                }
                propagateFocusLossFromGain_syncAf(i, focusRequester, z);
                this.mFocusStack.push(focusRequester);
                focusRequester.handleFocusGainFromRequest(z4 ? 1 : 0);
                notifyExtPolicyFocusGrant_syncAf(focusRequester.toAudioFocusInfo(), z4 ? 1 : 0);
                if ((i7 & true) != false) {
                    runAudioCheckerForRingOrCallAsync(z4);
                }
                return z4 ? 1 : 0;
            } catch (RemoteException unused) {
                int i8 = r15;
                Log.w(TAG, "AudioFocus  requestAudioFocus() could not link to " + iBinder + " binder death");
                return i8;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int abandonAudioFocus(IAudioFocusDispatcher iAudioFocusDispatcher, String str, AudioAttributes audioAttributes, String str2) {
        new MediaMetrics.Item(mMetricsId).setUid(Binder.getCallingUid()).set(MediaMetrics.Property.CALLING_PACKAGE, str2).set(MediaMetrics.Property.CLIENT_NAME, str).set(MediaMetrics.Property.EVENT, "abandonAudioFocus").record();
        mEventLogger.enqueue(new EventLogger.StringEvent("abandonAudioFocus() from uid/pid " + Binder.getCallingUid() + "/" + Binder.getCallingPid() + " clientId=" + str).printLog(TAG));
        try {
        } catch (ConcurrentModificationException e) {
            Log.e(TAG, "FATAL EXCEPTION AudioFocus  abandonAudioFocus() caused " + e);
            e.printStackTrace();
        }
        synchronized (mAudioFocusLock) {
            if (this.mFocusPolicy != null && notifyExtFocusPolicyFocusAbandon_syncAf(new AudioFocusInfo(audioAttributes, Binder.getCallingUid(), str, str2, 0, 0, 0, 0))) {
                return 1;
            }
            boolean z = this.mRingOrCallActive & ("AudioFocus_For_Phone_Ring_And_Calls".compareTo(str) == 0);
            if (z) {
                this.mRingOrCallActive = false;
            }
            removeFocusStackEntry(str, true, true);
            if (z & true) {
                runAudioCheckerForRingOrCallAsync(false);
            }
            return 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void unregisterAudioFocusClient(String str) {
        synchronized (mAudioFocusLock) {
            removeFocusStackEntry(str, false, true);
        }
    }

    private void runAudioCheckerForRingOrCallAsync(final boolean z) {
        new Thread() { // from class: com.android.server.audio.MediaFocusControl.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                if (z) {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                synchronized (MediaFocusControl.mAudioFocusLock) {
                    if (MediaFocusControl.this.mRingOrCallActive) {
                        MediaFocusControl.this.mFocusEnforcer.mutePlayersForCall(MediaFocusControl.USAGES_TO_MUTE_IN_RING_OR_CALL);
                    } else {
                        MediaFocusControl.this.mFocusEnforcer.unmutePlayersForCall();
                    }
                }
            }
        }.start();
    }

    public String getAudioFocusStack() {
        synchronized (mAudioFocusLock) {
            if (this.mFocusStack.isEmpty()) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            Iterator<FocusRequester> it = this.mFocusStack.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().getAudioFocusInfo());
            }
            return String.join("|", arrayList);
        }
    }

    public boolean isPackageInFocusStack(String str) {
        synchronized (mAudioFocusLock) {
            if (this.mFocusStack.size() <= 100 && str != null) {
                Iterator<FocusRequester> it = this.mFocusStack.iterator();
                while (it.hasNext()) {
                    if (str.equals(it.next().getPackageName())) {
                        return true;
                    }
                }
                if (this.mMultiAudioFocusEnabled) {
                    Iterator<FocusRequester> it2 = this.mMultiAudioFocusList.iterator();
                    while (it2.hasNext()) {
                        if (str.equals(it2.next().getPackageName())) {
                            return true;
                        }
                    }
                }
                Log.d(TAG, str + " do not in focus stack");
                return false;
            }
            return true;
        }
    }

    public void updateMultiAudioFocus(boolean z) {
        Log.d(TAG, "updateMultiAudioFocus( " + z + " )");
        this.mMultiAudioFocusEnabled = z;
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Settings.System.putIntForUser(contentResolver, "multi_audio_focus_enabled", z ? 1 : 0, contentResolver.getUserId());
        if (!this.mFocusStack.isEmpty()) {
            this.mFocusStack.peek().handleFocusLoss(-1, null, false);
        }
        if (z || this.mMultiAudioFocusList.isEmpty()) {
            return;
        }
        Iterator<FocusRequester> it = this.mMultiAudioFocusList.iterator();
        while (it.hasNext()) {
            it.next().handleFocusLoss(-1, null, false);
        }
        this.mMultiAudioFocusList.clear();
    }

    public boolean getMultiAudioFocusEnabled() {
        return this.mMultiAudioFocusEnabled;
    }

    private void dumpMultiAudioFocus(PrintWriter printWriter) {
        printWriter.println("Multi Audio Focus enabled :" + this.mMultiAudioFocusEnabled);
        if (this.mMultiAudioFocusList.isEmpty()) {
            return;
        }
        printWriter.println("Multi Audio Focus List:");
        printWriter.println("------------------------------");
        Iterator<FocusRequester> it = this.mMultiAudioFocusList.iterator();
        while (it.hasNext()) {
            it.next().dump(printWriter);
        }
        printWriter.println("------------------------------");
    }

    void postDelayedLossAfterFade(FocusRequester focusRequester, long j) {
        if (DEBUG) {
            Log.v(TAG, "postDelayedLossAfterFade loser=" + focusRequester.getPackageName() + ", isInFocusLossLimbo=" + focusRequester.isInFocusLossLimbo() + "delayMs=" + j);
        }
        Handler handler = this.mFocusHandler;
        handler.sendMessageDelayed(handler.obtainMessage(1, focusRequester), 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postForgetUidLater(int i) {
        Handler handler = this.mFocusHandler;
        handler.sendMessageDelayed(handler.obtainMessage(2, new ForgetFadeUidInfo(i)), 2000L);
    }

    private void initFocusThreading() {
        HandlerThread handlerThread = new HandlerThread(TAG);
        this.mFocusThread = handlerThread;
        handlerThread.start();
        this.mFocusHandler = new Handler(this.mFocusThread.getLooper()) { // from class: com.android.server.audio.MediaFocusControl.3
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i != 1) {
                    if (i != 2) {
                        return;
                    }
                    int i2 = ((ForgetFadeUidInfo) message.obj).mUid;
                    if (MediaFocusControl.DEBUG) {
                        Log.d(MediaFocusControl.TAG, "MSL_L_FORGET_UID uid=" + i2);
                    }
                    MediaFocusControl.this.mFocusEnforcer.forgetUid(i2);
                    return;
                }
                if (MediaFocusControl.DEBUG) {
                    Log.d(MediaFocusControl.TAG, "MSG_L_FOCUS_LOSS_AFTER_FADE loser=" + ((FocusRequester) message.obj).getPackageName());
                }
                synchronized (MediaFocusControl.mAudioFocusLock) {
                    FocusRequester focusRequester = (FocusRequester) message.obj;
                    if (focusRequester.isInFocusLossLimbo()) {
                        focusRequester.dispatchFocusChange(-1);
                        MediaFocusControl.this.mFocusEnforcer.restoreVShapedPlayers((FocusRequester) message.obj);
                        focusRequester.release();
                        MediaFocusControl.this.postForgetUidLater(focusRequester.getClientUid());
                    }
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class ForgetFadeUidInfo {
        private final int mUid;

        ForgetFadeUidInfo(int i) {
            this.mUid = i;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && ForgetFadeUidInfo.class == obj.getClass() && ((ForgetFadeUidInfo) obj).mUid == this.mUid;
        }

        public int hashCode() {
            return this.mUid;
        }
    }
}
