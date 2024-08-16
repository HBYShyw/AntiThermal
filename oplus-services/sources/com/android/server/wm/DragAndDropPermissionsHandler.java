package com.android.server.wm;

import android.app.UriGrantsManager;
import android.content.ClipData;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import com.android.internal.view.IDragAndDropPermissions;
import com.android.server.LocalServices;
import com.android.server.uri.UriGrantsManagerInternal;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DragAndDropPermissionsHandler extends IDragAndDropPermissions.Stub {
    private static final boolean DEBUG = false;
    private static final String TAG = "DragAndDrop";
    private IBinder mActivityToken;
    private final WindowManagerGlobalLock mGlobalLock;
    private final int mMode;
    private IBinder mPermissionOwnerToken;
    private final int mSourceUid;
    private final int mSourceUserId;
    private final String mTargetPackage;
    private final int mTargetUserId;
    private final ArrayList<Uri> mUris;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DragAndDropPermissionsHandler(WindowManagerGlobalLock windowManagerGlobalLock, ClipData clipData, int i, String str, int i2, int i3, int i4) {
        ArrayList<Uri> arrayList = new ArrayList<>();
        this.mUris = arrayList;
        this.mActivityToken = null;
        this.mPermissionOwnerToken = null;
        this.mGlobalLock = windowManagerGlobalLock;
        this.mSourceUid = i;
        this.mTargetPackage = str;
        this.mMode = i2;
        this.mSourceUserId = i3;
        this.mTargetUserId = i4;
        clipData.collectUris(arrayList);
    }

    public void take(IBinder iBinder) throws RemoteException {
        if (this.mActivityToken == null && this.mPermissionOwnerToken == null) {
            this.mActivityToken = iBinder;
            doTake(getUriPermissionOwnerForActivity(iBinder));
        }
    }

    private void doTake(IBinder iBinder) throws RemoteException {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        for (int i = 0; i < this.mUris.size(); i++) {
            try {
                UriGrantsManager.getService().grantUriPermissionFromOwner(iBinder, this.mSourceUid, this.mTargetPackage, this.mUris.get(i), this.mMode, this.mSourceUserId, this.mTargetUserId);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public void takeTransient() throws RemoteException {
        if (this.mActivityToken == null && this.mPermissionOwnerToken == null) {
            IBinder newUriPermissionOwner = ((UriGrantsManagerInternal) LocalServices.getService(UriGrantsManagerInternal.class)).newUriPermissionOwner("drop");
            this.mPermissionOwnerToken = newUriPermissionOwner;
            doTake(newUriPermissionOwner);
        }
    }

    public void release() throws RemoteException {
        IBinder uriPermissionOwnerForActivity;
        IBinder iBinder = this.mActivityToken;
        if (iBinder == null && this.mPermissionOwnerToken == null) {
            return;
        }
        if (iBinder != null) {
            try {
                uriPermissionOwnerForActivity = getUriPermissionOwnerForActivity(iBinder);
            } catch (Exception unused) {
                return;
            } finally {
                this.mActivityToken = null;
            }
        } else {
            uriPermissionOwnerForActivity = this.mPermissionOwnerToken;
            this.mPermissionOwnerToken = null;
        }
        UriGrantsManagerInternal uriGrantsManagerInternal = (UriGrantsManagerInternal) LocalServices.getService(UriGrantsManagerInternal.class);
        for (int i = 0; i < this.mUris.size(); i++) {
            uriGrantsManagerInternal.revokeUriPermissionFromOwner(uriPermissionOwnerForActivity, this.mUris.get(i), this.mMode, this.mSourceUserId);
        }
    }

    private IBinder getUriPermissionOwnerForActivity(IBinder iBinder) {
        Binder externalToken;
        ActivityTaskManagerService.enforceNotIsolatedCaller("getUriPermissionOwnerForActivity");
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ActivityRecord isInRootTaskLocked = ActivityRecord.isInRootTaskLocked(iBinder);
                if (isInRootTaskLocked == null) {
                    throw new IllegalArgumentException("Activity does not exist; token=" + iBinder);
                }
                externalToken = isInRootTaskLocked.getUriPermissionsLocked().getExternalToken();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return externalToken;
    }

    protected void finalize() throws Throwable {
        if (this.mActivityToken != null || this.mPermissionOwnerToken == null) {
            return;
        }
        release();
    }
}
