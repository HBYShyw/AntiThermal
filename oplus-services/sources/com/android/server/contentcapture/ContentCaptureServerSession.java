package com.android.server.contentcapture;

import android.app.assist.ActivityId;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.service.contentcapture.ContentCaptureService;
import android.service.contentcapture.SnapshotData;
import android.util.LocalLog;
import android.util.Slog;
import android.view.contentcapture.ContentCaptureContext;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.IResultReceiver;
import com.android.internal.util.Preconditions;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ContentCaptureServerSession {
    private static final String TAG = "ContentCaptureServerSession";
    public final ComponentName appComponentName;
    final IBinder mActivityToken;
    private final ContentCaptureContext mContentCaptureContext;
    private final int mId;
    private final Object mLock;
    private final ContentCapturePerUserService mService;
    private final IResultReceiver mSessionStateReceiver;
    private final int mUid;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentCaptureServerSession(Object obj, IBinder iBinder, ActivityId activityId, ContentCapturePerUserService contentCapturePerUserService, ComponentName componentName, IResultReceiver iResultReceiver, int i, int i2, int i3, int i4, int i5) {
        Preconditions.checkArgument(i3 != 0);
        this.mLock = obj;
        this.mActivityToken = iBinder;
        this.appComponentName = componentName;
        this.mService = contentCapturePerUserService;
        this.mId = i3;
        this.mUid = i4;
        this.mContentCaptureContext = new ContentCaptureContext(null, activityId, componentName, i2, iBinder, i5);
        this.mSessionStateReceiver = iResultReceiver;
        try {
            iResultReceiver.asBinder().linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.contentcapture.ContentCaptureServerSession$$ExternalSyntheticLambda0
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    ContentCaptureServerSession.this.lambda$new$0();
                }
            }, 0);
        } catch (Exception unused) {
            Slog.w(TAG, "could not register DeathRecipient for " + iBinder);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isActivitySession(IBinder iBinder) {
        return this.mActivityToken.equals(iBinder);
    }

    @GuardedBy({"mLock"})
    public void notifySessionStartedLocked(IResultReceiver iResultReceiver) {
        RemoteContentCaptureService remoteContentCaptureService = this.mService.mRemoteService;
        if (remoteContentCaptureService == null) {
            Slog.w(TAG, "notifySessionStartedLocked(): no remote service");
        } else {
            remoteContentCaptureService.onSessionStarted(this.mContentCaptureContext, this.mId, this.mUid, iResultReceiver, 2);
        }
    }

    @GuardedBy({"mLock"})
    public void setContentCaptureEnabledLocked(boolean z) {
        try {
            Bundle bundle = new Bundle();
            int i = 1;
            bundle.putBoolean("enabled", true);
            IResultReceiver iResultReceiver = this.mSessionStateReceiver;
            if (!z) {
                i = 2;
            }
            iResultReceiver.send(i, bundle);
        } catch (RemoteException e) {
            Slog.w(TAG, "Error async reporting result to client: " + e);
        }
    }

    @GuardedBy({"mLock"})
    public void sendActivitySnapshotLocked(SnapshotData snapshotData) {
        LocalLog localLog = ((ContentCaptureManagerService) this.mService.getMaster()).mRequestsHistory;
        if (localLog != null) {
            localLog.log("snapshot: id=" + this.mId);
        }
        RemoteContentCaptureService remoteContentCaptureService = this.mService.mRemoteService;
        if (remoteContentCaptureService == null) {
            Slog.w(TAG, "sendActivitySnapshotLocked(): no remote service");
        } else {
            remoteContentCaptureService.onActivitySnapshotRequest(this.mId, snapshotData);
        }
    }

    @GuardedBy({"mLock"})
    public void removeSelfLocked(boolean z) {
        try {
            destroyLocked(z);
        } finally {
            this.mService.removeSessionLocked(this.mId);
        }
    }

    @GuardedBy({"mLock"})
    public void destroyLocked(boolean z) {
        if (this.mService.isVerbose()) {
            Slog.v(TAG, "destroy(notifyRemoteService=" + z + ")");
        }
        if (z) {
            RemoteContentCaptureService remoteContentCaptureService = this.mService.mRemoteService;
            if (remoteContentCaptureService == null) {
                Slog.w(TAG, "destroyLocked(): no remote service");
            } else {
                remoteContentCaptureService.onSessionFinished(this.mId);
            }
        }
    }

    @GuardedBy({"mLock"})
    public void resurrectLocked() {
        ContentCapturePerUserService contentCapturePerUserService = this.mService;
        RemoteContentCaptureService remoteContentCaptureService = contentCapturePerUserService.mRemoteService;
        if (remoteContentCaptureService == null) {
            Slog.w(TAG, "destroyLocked(: no remote service");
            return;
        }
        if (contentCapturePerUserService.isVerbose()) {
            Slog.v(TAG, "resurrecting " + this.mActivityToken + " on " + remoteContentCaptureService);
        }
        remoteContentCaptureService.onSessionStarted(new ContentCaptureContext(this.mContentCaptureContext, 4), this.mId, this.mUid, this.mSessionStateReceiver, 4098);
    }

    @GuardedBy({"mLock"})
    public void pauseLocked() {
        if (this.mService.isVerbose()) {
            Slog.v(TAG, "pausing " + this.mActivityToken);
        }
        ContentCaptureService.setClientState(this.mSessionStateReceiver, 2052, (IBinder) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onClientDeath, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        if (this.mService.isVerbose()) {
            Slog.v(TAG, "onClientDeath(" + this.mActivityToken + "): removing session " + this.mId);
        }
        synchronized (this.mLock) {
            removeSelfLocked(true);
        }
    }

    @GuardedBy({"mLock"})
    public void dumpLocked(String str, PrintWriter printWriter) {
        printWriter.print(str);
        printWriter.print("id: ");
        printWriter.print(this.mId);
        printWriter.println();
        printWriter.print(str);
        printWriter.print("uid: ");
        printWriter.print(this.mUid);
        printWriter.println();
        printWriter.print(str);
        printWriter.print("context: ");
        this.mContentCaptureContext.dump(printWriter);
        printWriter.println();
        printWriter.print(str);
        printWriter.print("activity token: ");
        printWriter.println(this.mActivityToken);
        printWriter.print(str);
        printWriter.print("app component: ");
        printWriter.println(this.appComponentName);
        printWriter.print(str);
        printWriter.print("has autofill callback: ");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String toShortString() {
        return this.mId + ":" + this.mActivityToken;
    }

    public String toString() {
        return "ContentCaptureSession[id=" + this.mId + ", act=" + this.mActivityToken + "]";
    }
}
