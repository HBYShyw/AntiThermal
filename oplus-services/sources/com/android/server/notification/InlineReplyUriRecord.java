package com.android.server.notification;

import android.app.ActivityManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.ArraySet;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class InlineReplyUriRecord {
    private final String mKey;
    private final String mPackageName;
    private final IBinder mPermissionOwner;
    private final ArraySet<Uri> mUris = new ArraySet<>();
    private final UserHandle mUser;

    public InlineReplyUriRecord(IBinder iBinder, UserHandle userHandle, String str, String str2) {
        this.mPermissionOwner = iBinder;
        this.mUser = userHandle;
        this.mPackageName = str;
        this.mKey = str2;
    }

    public IBinder getPermissionOwner() {
        return this.mPermissionOwner;
    }

    public ArraySet<Uri> getUris() {
        return this.mUris;
    }

    public void addUri(Uri uri) {
        this.mUris.add(uri);
    }

    public int getUserId() {
        int identifier = this.mUser.getIdentifier();
        if (UserManager.isHeadlessSystemUserMode() && identifier == -1) {
            return ActivityManager.getCurrentUser();
        }
        if (identifier == -1) {
            return 0;
        }
        return identifier;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String getKey() {
        return this.mKey;
    }
}
