package com.android.server.contentsuggestions;

import android.R;
import android.app.contentsuggestions.ClassificationsRequest;
import android.app.contentsuggestions.IClassificationsCallback;
import android.app.contentsuggestions.IContentSuggestionsManager;
import android.app.contentsuggestions.ISelectionsCallback;
import android.app.contentsuggestions.SelectionsRequest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.hardware.HardwareBuffer;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.UserHandle;
import android.util.Slog;
import android.window.TaskSnapshot;
import com.android.internal.os.IResultReceiver;
import com.android.server.LocalServices;
import com.android.server.infra.AbstractMasterSystemService;
import com.android.server.infra.FrameworkResourcesServiceNameResolver;
import com.android.server.wm.ActivityTaskManagerInternal;
import java.io.FileDescriptor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ContentSuggestionsManagerService extends AbstractMasterSystemService<ContentSuggestionsManagerService, ContentSuggestionsPerUserService> {
    private static final String EXTRA_BITMAP = "android.contentsuggestions.extra.BITMAP";
    private static final int MAX_TEMP_SERVICE_DURATION_MS = 120000;
    private static final String TAG = "ContentSuggestionsManagerService";
    private static final boolean VERBOSE = false;
    private ActivityTaskManagerInternal mActivityTaskManagerInternal;

    protected int getMaximumTemporaryServiceDurationMs() {
        return 120000;
    }

    public ContentSuggestionsManagerService(Context context) {
        super(context, new FrameworkResourcesServiceNameResolver(context, R.string.config_mobile_hotspot_provision_app_no_ui), "no_content_suggestions");
        this.mActivityTaskManagerInternal = (ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: newServiceLocked, reason: merged with bridge method [inline-methods] */
    public ContentSuggestionsPerUserService m3108newServiceLocked(int i, boolean z) {
        return new ContentSuggestionsPerUserService(this, ((AbstractMasterSystemService) this).mLock, i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onStart() {
        publishBinderService("content_suggestions", new ContentSuggestionsManagerStub());
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.MANAGE_CONTENT_SUGGESTIONS", TAG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void enforceCaller(int i, String str) {
        if (getContext().checkCallingPermission("android.permission.MANAGE_CONTENT_SUGGESTIONS") == 0 || ((AbstractMasterSystemService) this).mServiceNameResolver.isTemporary(i) || this.mActivityTaskManagerInternal.isCallerRecents(Binder.getCallingUid())) {
            return;
        }
        String str2 = "Permission Denial: " + str + " from pid=" + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid() + " expected caller is recents";
        Slog.w(TAG, str2);
        throw new SecurityException(str2);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class ContentSuggestionsManagerStub extends IContentSuggestionsManager.Stub {
        private ContentSuggestionsManagerStub() {
        }

        public void provideContextBitmap(int i, Bitmap bitmap, Bundle bundle) {
            if (bitmap == null) {
                throw new IllegalArgumentException("Expected non-null bitmap");
            }
            if (bundle == null) {
                throw new IllegalArgumentException("Expected non-null imageContextRequestExtras");
            }
            ContentSuggestionsManagerService.this.enforceCaller(UserHandle.getCallingUserId(), "provideContextBitmap");
            synchronized (((AbstractMasterSystemService) ContentSuggestionsManagerService.this).mLock) {
                ContentSuggestionsPerUserService contentSuggestionsPerUserService = (ContentSuggestionsPerUserService) ContentSuggestionsManagerService.this.getServiceForUserLocked(i);
                if (contentSuggestionsPerUserService != null) {
                    bundle.putParcelable(ContentSuggestionsManagerService.EXTRA_BITMAP, bitmap);
                    contentSuggestionsPerUserService.provideContextImageFromBitmapLocked(bundle);
                }
            }
        }

        public void provideContextImage(int i, int i2, Bundle bundle) {
            HardwareBuffer hardwareBuffer;
            TaskSnapshot taskSnapshotBlocking;
            if (bundle == null) {
                throw new IllegalArgumentException("Expected non-null imageContextRequestExtras");
            }
            ContentSuggestionsManagerService.this.enforceCaller(UserHandle.getCallingUserId(), "provideContextImage");
            int i3 = 0;
            if (bundle.containsKey(ContentSuggestionsManagerService.EXTRA_BITMAP) || (taskSnapshotBlocking = ContentSuggestionsManagerService.this.mActivityTaskManagerInternal.getTaskSnapshotBlocking(i2, false)) == null) {
                hardwareBuffer = null;
            } else {
                hardwareBuffer = taskSnapshotBlocking.getHardwareBuffer();
                ColorSpace colorSpace = taskSnapshotBlocking.getColorSpace();
                if (colorSpace != null) {
                    i3 = colorSpace.getId();
                }
            }
            synchronized (((AbstractMasterSystemService) ContentSuggestionsManagerService.this).mLock) {
                ContentSuggestionsPerUserService contentSuggestionsPerUserService = (ContentSuggestionsPerUserService) ContentSuggestionsManagerService.this.getServiceForUserLocked(i);
                if (contentSuggestionsPerUserService != null) {
                    contentSuggestionsPerUserService.provideContextImageLocked(i2, hardwareBuffer, i3, bundle);
                }
            }
        }

        public void suggestContentSelections(int i, SelectionsRequest selectionsRequest, ISelectionsCallback iSelectionsCallback) {
            ContentSuggestionsManagerService.this.enforceCaller(UserHandle.getCallingUserId(), "suggestContentSelections");
            synchronized (((AbstractMasterSystemService) ContentSuggestionsManagerService.this).mLock) {
                ContentSuggestionsPerUserService contentSuggestionsPerUserService = (ContentSuggestionsPerUserService) ContentSuggestionsManagerService.this.getServiceForUserLocked(i);
                if (contentSuggestionsPerUserService != null) {
                    contentSuggestionsPerUserService.suggestContentSelectionsLocked(selectionsRequest, iSelectionsCallback);
                }
            }
        }

        public void classifyContentSelections(int i, ClassificationsRequest classificationsRequest, IClassificationsCallback iClassificationsCallback) {
            ContentSuggestionsManagerService.this.enforceCaller(UserHandle.getCallingUserId(), "classifyContentSelections");
            synchronized (((AbstractMasterSystemService) ContentSuggestionsManagerService.this).mLock) {
                ContentSuggestionsPerUserService contentSuggestionsPerUserService = (ContentSuggestionsPerUserService) ContentSuggestionsManagerService.this.getServiceForUserLocked(i);
                if (contentSuggestionsPerUserService != null) {
                    contentSuggestionsPerUserService.classifyContentSelectionsLocked(classificationsRequest, iClassificationsCallback);
                }
            }
        }

        public void notifyInteraction(int i, String str, Bundle bundle) {
            ContentSuggestionsManagerService.this.enforceCaller(UserHandle.getCallingUserId(), "notifyInteraction");
            synchronized (((AbstractMasterSystemService) ContentSuggestionsManagerService.this).mLock) {
                ContentSuggestionsPerUserService contentSuggestionsPerUserService = (ContentSuggestionsPerUserService) ContentSuggestionsManagerService.this.getServiceForUserLocked(i);
                if (contentSuggestionsPerUserService != null) {
                    contentSuggestionsPerUserService.notifyInteractionLocked(str, bundle);
                }
            }
        }

        public void isEnabled(int i, IResultReceiver iResultReceiver) throws RemoteException {
            boolean isDisabledLocked;
            ContentSuggestionsManagerService.this.enforceCaller(UserHandle.getCallingUserId(), "isEnabled");
            synchronized (((AbstractMasterSystemService) ContentSuggestionsManagerService.this).mLock) {
                isDisabledLocked = ContentSuggestionsManagerService.this.isDisabledLocked(i);
            }
            iResultReceiver.send(!isDisabledLocked ? 1 : 0, (Bundle) null);
        }

        public void resetTemporaryService(int i) {
            ContentSuggestionsManagerService.this.resetTemporaryService(i);
        }

        public void setTemporaryService(int i, String str, int i2) {
            ContentSuggestionsManagerService.this.setTemporaryService(i, str, i2);
        }

        public void setDefaultServiceEnabled(int i, boolean z) {
            ContentSuggestionsManagerService.this.setDefaultServiceEnabled(i, z);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) throws RemoteException {
            int callingUid = Binder.getCallingUid();
            if (callingUid != 2000 && callingUid != 0) {
                Slog.e(ContentSuggestionsManagerService.TAG, "Expected shell caller");
            } else {
                new ContentSuggestionsManagerServiceShellCommand(ContentSuggestionsManagerService.this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
            }
        }
    }
}
