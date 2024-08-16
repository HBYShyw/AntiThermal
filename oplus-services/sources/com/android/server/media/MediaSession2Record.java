package com.android.server.media;

import android.media.MediaController2;
import android.media.Session2CommandGroup;
import android.media.Session2Token;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.os.UserHandle;
import android.util.Log;
import android.view.KeyEvent;
import com.android.internal.annotations.GuardedBy;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class MediaSession2Record implements MediaSessionRecordImpl {

    @GuardedBy({"mLock"})
    private final MediaController2 mController;

    @GuardedBy({"mLock"})
    private final HandlerExecutor mHandlerExecutor;

    @GuardedBy({"mLock"})
    private boolean mIsClosed;

    @GuardedBy({"mLock"})
    private boolean mIsConnected;
    private final Object mLock;

    @GuardedBy({"mLock"})
    private int mPolicies;

    @GuardedBy({"mLock"})
    private final MediaSessionService mService;

    @GuardedBy({"mLock"})
    private final Session2Token mSessionToken;
    private static final String TAG = "MediaSession2Record";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);

    @Override // com.android.server.media.MediaSessionRecordImpl
    public void adjustVolume(String str, String str2, int i, int i2, boolean z, int i3, int i4, boolean z2) {
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean canHandleVolumeKey() {
        return false;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean isPlaybackTypeLocal() {
        return true;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean isSystemPriority() {
        return false;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean sendMediaButton(String str, int i, int i2, boolean z, KeyEvent keyEvent, int i3, ResultReceiver resultReceiver) {
        return false;
    }

    public MediaSession2Record(Session2Token session2Token, MediaSessionService mediaSessionService, Looper looper, int i) {
        Object obj = new Object();
        this.mLock = obj;
        synchronized (obj) {
            this.mSessionToken = session2Token;
            this.mService = mediaSessionService;
            HandlerExecutor handlerExecutor = new HandlerExecutor(new Handler(looper));
            this.mHandlerExecutor = handlerExecutor;
            this.mController = new MediaController2.Builder(mediaSessionService.getContext(), session2Token).setControllerCallback(handlerExecutor, new Controller2Callback()).build();
            this.mPolicies = i;
        }
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public String getPackageName() {
        return this.mSessionToken.getPackageName();
    }

    public Session2Token getSession2Token() {
        return this.mSessionToken;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public int getUid() {
        return this.mSessionToken.getUid();
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public int getUserId() {
        return UserHandle.getUserHandleForUid(this.mSessionToken.getUid()).getIdentifier();
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean isActive() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsConnected;
        }
        return z;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean checkPlaybackActiveState(boolean z) {
        boolean z2;
        synchronized (this.mLock) {
            z2 = this.mIsConnected && this.mController.isPlaybackActive() == z;
        }
        return z2;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl, java.lang.AutoCloseable
    public void close() {
        synchronized (this.mLock) {
            this.mIsClosed = true;
            this.mController.close();
        }
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean isClosed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsClosed;
        }
        return z;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public int getSessionPolicies() {
        int i;
        synchronized (this.mLock) {
            i = this.mPolicies;
        }
        return i;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public void setSessionPolicies(int i) {
        synchronized (this.mLock) {
            this.mPolicies = i;
        }
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + "token=" + this.mSessionToken);
        printWriter.println(str + "controller=" + this.mController);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("  ");
        printWriter.println(sb.toString() + "playbackActive=" + this.mController.isPlaybackActive());
    }

    public String toString() {
        return getPackageName() + " (userId=" + getUserId() + ")";
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class Controller2Callback extends MediaController2.ControllerCallback {
        private Controller2Callback() {
        }

        @Override // android.media.MediaController2.ControllerCallback
        public void onConnected(MediaController2 mediaController2, Session2CommandGroup session2CommandGroup) {
            MediaSessionService mediaSessionService;
            if (MediaSession2Record.DEBUG) {
                Log.d(MediaSession2Record.TAG, "connected to " + MediaSession2Record.this.mSessionToken + ", allowed=" + session2CommandGroup);
            }
            synchronized (MediaSession2Record.this.mLock) {
                MediaSession2Record.this.mIsConnected = true;
                mediaSessionService = MediaSession2Record.this.mService;
            }
            mediaSessionService.onSessionActiveStateChanged(MediaSession2Record.this);
        }

        @Override // android.media.MediaController2.ControllerCallback
        public void onDisconnected(MediaController2 mediaController2) {
            MediaSessionService mediaSessionService;
            if (MediaSession2Record.DEBUG) {
                Log.d(MediaSession2Record.TAG, "disconnected from " + MediaSession2Record.this.mSessionToken);
            }
            synchronized (MediaSession2Record.this.mLock) {
                MediaSession2Record.this.mIsConnected = false;
                mediaSessionService = MediaSession2Record.this.mService;
            }
            mediaSessionService.onSessionDied(MediaSession2Record.this);
        }

        @Override // android.media.MediaController2.ControllerCallback
        public void onPlaybackActiveChanged(MediaController2 mediaController2, boolean z) {
            MediaSessionService mediaSessionService;
            if (MediaSession2Record.DEBUG) {
                Log.d(MediaSession2Record.TAG, "playback active changed, " + MediaSession2Record.this.mSessionToken + ", active=" + z);
            }
            synchronized (MediaSession2Record.this.mLock) {
                mediaSessionService = MediaSession2Record.this.mService;
            }
            mediaSessionService.onSessionPlaybackStateChanged(MediaSession2Record.this, z);
        }
    }
}
