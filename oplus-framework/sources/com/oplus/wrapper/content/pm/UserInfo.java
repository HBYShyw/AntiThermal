package com.oplus.wrapper.content.pm;

import android.os.UserHandle;

/* loaded from: classes.dex */
public class UserInfo {
    private final android.content.pm.UserInfo mUserInfo;

    public UserInfo(android.content.pm.UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    public int getId() {
        return this.mUserInfo.id;
    }

    public UserHandle getUserHandle() {
        return this.mUserInfo.getUserHandle();
    }

    public boolean isEnabled() {
        return this.mUserInfo.isEnabled();
    }

    public android.content.pm.UserInfo getUserInfo() {
        return this.mUserInfo;
    }
}
