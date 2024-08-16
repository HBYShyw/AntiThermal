package com.oplus.wrapper.os;

import com.oplus.wrapper.content.pm.UserInfo;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class UserManager {
    private final android.os.UserManager mUserManager;

    public UserManager(android.os.UserManager userManager) {
        this.mUserManager = userManager;
    }

    public List<UserInfo> getUsers() {
        List<android.content.pm.UserInfo> users = this.mUserManager.getUsers();
        if (users == null) {
            return null;
        }
        List<UserInfo> userInfoList = new ArrayList<>(users.size());
        for (android.content.pm.UserInfo userInfo : users) {
            if (userInfo != null) {
                userInfoList.add(new UserInfo(userInfo));
            }
        }
        return userInfoList;
    }

    public UserInfo getUserInfo(int userId) {
        if (this.mUserManager.getUserInfo(userId) == null) {
            return null;
        }
        return new UserInfo(this.mUserManager.getUserInfo(userId));
    }

    public UserInfo createUser(String name, int flags) {
        android.content.pm.UserInfo userInfo = this.mUserManager.createUser(name, flags);
        if (userInfo == null) {
            return null;
        }
        return new UserInfo(userInfo);
    }

    public boolean removeUser(int userId) {
        return this.mUserManager.removeUser(userId);
    }

    public boolean isUserUnlockingOrUnlocked(android.os.UserHandle user) {
        return this.mUserManager.isUserUnlockingOrUnlocked(user);
    }

    public boolean isGuestUser() {
        return this.mUserManager.isGuestUser();
    }

    public boolean canAddMoreUsers() {
        return this.mUserManager.canAddMoreUsers();
    }

    public boolean hasUserRestrictionForUser(String restrictionKey, android.os.UserHandle userHandle) {
        return this.mUserManager.hasUserRestrictionForUser(restrictionKey, userHandle);
    }

    public boolean isManagedProfile(int userId) {
        return this.mUserManager.isManagedProfile(userId);
    }
}
