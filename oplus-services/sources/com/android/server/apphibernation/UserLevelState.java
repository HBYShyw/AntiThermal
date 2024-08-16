package com.android.server.apphibernation;

import java.text.SimpleDateFormat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class UserLevelState {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public boolean hibernated;
    public long lastUnhibernatedMs;
    public String packageName;
    public long savedByte;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserLevelState() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserLevelState(UserLevelState userLevelState) {
        this.packageName = userLevelState.packageName;
        this.hibernated = userLevelState.hibernated;
        this.savedByte = userLevelState.savedByte;
        this.lastUnhibernatedMs = userLevelState.lastUnhibernatedMs;
    }

    public String toString() {
        return "UserLevelState{packageName='" + this.packageName + "', hibernated=" + this.hibernated + "', savedByte=" + this.savedByte + "', lastUnhibernated=" + DATE_FORMAT.format(Long.valueOf(this.lastUnhibernatedMs)) + '}';
    }
}
