package com.android.server.inputmethod;

import android.app.UriGrantsManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.inputmethod.IInputContentUriToken;
import com.android.server.LocalServices;
import com.android.server.uri.UriGrantsManagerInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class InputContentUriTokenHandler extends IInputContentUriToken.Stub {
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private IBinder mPermissionOwnerToken = null;
    private final int mSourceUid;
    private final int mSourceUserId;
    private final String mTargetPackage;
    private final int mTargetUserId;
    private final Uri mUri;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputContentUriTokenHandler(Uri uri, int i, String str, int i2, int i3) {
        this.mUri = uri;
        this.mSourceUid = i;
        this.mTargetPackage = str;
        this.mSourceUserId = i2;
        this.mTargetUserId = i3;
    }

    public void take() {
        synchronized (this.mLock) {
            if (this.mPermissionOwnerToken != null) {
                return;
            }
            IBinder newUriPermissionOwner = ((UriGrantsManagerInternal) LocalServices.getService(UriGrantsManagerInternal.class)).newUriPermissionOwner("InputContentUriTokenHandler");
            this.mPermissionOwnerToken = newUriPermissionOwner;
            doTakeLocked(newUriPermissionOwner);
        }
    }

    private void doTakeLocked(IBinder iBinder) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                UriGrantsManager.getService().grantUriPermissionFromOwner(iBinder, this.mSourceUid, this.mTargetPackage, this.mUri, 1, this.mSourceUserId, this.mTargetUserId);
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void release() {
        synchronized (this.mLock) {
            if (this.mPermissionOwnerToken == null) {
                return;
            }
            try {
                ((UriGrantsManagerInternal) LocalServices.getService(UriGrantsManagerInternal.class)).revokeUriPermissionFromOwner(this.mPermissionOwnerToken, this.mUri, 1, this.mSourceUserId);
            } finally {
                this.mPermissionOwnerToken = null;
            }
        }
    }

    protected void finalize() throws Throwable {
        try {
            release();
        } finally {
            super/*java.lang.Object*/.finalize();
        }
    }
}
