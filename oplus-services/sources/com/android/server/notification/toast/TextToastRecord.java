package com.android.server.notification.toast;

import android.app.ITransientNotificationCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.UserHandle;
import android.util.Slog;
import com.android.internal.util.Preconditions;
import com.android.server.notification.NotificationManagerService;
import com.android.server.slice.SliceClientPermissions;
import com.android.server.statusbar.StatusBarManagerInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class TextToastRecord extends ToastRecord {
    private static final String TAG = "NotificationService";
    private final ITransientNotificationCallback mCallback;
    private final StatusBarManagerInternal mStatusBar;
    public final CharSequence text;

    @Override // com.android.server.notification.toast.ToastRecord
    public boolean isAppRendered() {
        return false;
    }

    public TextToastRecord(NotificationManagerService notificationManagerService, StatusBarManagerInternal statusBarManagerInternal, int i, int i2, String str, boolean z, IBinder iBinder, CharSequence charSequence, int i3, Binder binder, int i4, ITransientNotificationCallback iTransientNotificationCallback) {
        super(notificationManagerService, i, i2, str, z, iBinder, i3, binder, i4);
        this.mStatusBar = statusBarManagerInternal;
        this.mCallback = iTransientNotificationCallback;
        this.text = (CharSequence) Preconditions.checkNotNull(charSequence);
    }

    @Override // com.android.server.notification.toast.ToastRecord
    public boolean show() {
        if (NotificationManagerService.DBG) {
            Slog.d("NotificationService", "Show pkg=" + this.pkg + " text=" + ((Object) this.text));
        }
        StatusBarManagerInternal statusBarManagerInternal = this.mStatusBar;
        if (statusBarManagerInternal == null) {
            Slog.w("NotificationService", "StatusBar not available to show text toast for package " + this.pkg);
            return false;
        }
        statusBarManagerInternal.showToast(this.uid, this.pkg, this.token, this.text, this.windowToken, getDuration(), this.mCallback, this.displayId);
        return true;
    }

    @Override // com.android.server.notification.toast.ToastRecord
    public void hide() {
        Preconditions.checkNotNull(this.mStatusBar, "Cannot hide toast that wasn't shown");
        this.mStatusBar.hideToast(this.pkg, this.token);
    }

    public String toString() {
        return "TextToastRecord{" + Integer.toHexString(System.identityHashCode(this)) + " " + this.pid + ":" + this.pkg + SliceClientPermissions.SliceAuthority.DELIMITER + UserHandle.formatUid(this.uid) + " isSystemToast=" + this.isSystemToast + " token=" + this.token + " text=" + ((Object) this.text) + " duration=" + getDuration() + "}";
    }
}
