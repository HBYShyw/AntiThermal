package com.android.server.pm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DumpState {
    public static final int DUMP_ACTIVITY_RESOLVERS = 4;
    public static final int DUMP_APEX = 33554432;
    public static final int DUMP_CHANGES = 4194304;
    public static final int DUMP_COMPILER_STATS = 2097152;
    public static final int DUMP_CONTENT_RESOLVERS = 32;
    public static final int DUMP_DEXOPT = 1048576;
    public static final int DUMP_DOMAIN_PREFERRED = 262144;
    public static final int DUMP_DOMAIN_VERIFIER = 131072;
    public static final int DUMP_FEATURES = 2;
    public static final int DUMP_FROZEN = 524288;
    public static final int DUMP_INSTALLS = 65536;
    public static final int DUMP_KEYSETS = 16384;
    public static final int DUMP_KNOWN_PACKAGES = 134217728;
    public static final int DUMP_LIBS = 1;
    public static final int DUMP_MESSAGES = 512;
    public static final int DUMP_PACKAGES = 128;
    public static final int DUMP_PERMISSIONS = 64;
    public static final int DUMP_PER_UID_READ_TIMEOUTS = 268435456;
    public static final int DUMP_PREFERRED = 4096;
    public static final int DUMP_PREFERRED_XML = 8192;
    public static final int DUMP_PROTECTED_BROADCASTS = 1073741824;
    public static final int DUMP_PROVIDERS = 1024;
    public static final int DUMP_QUERIES = 67108864;
    public static final int DUMP_RECEIVER_RESOLVERS = 16;
    public static final int DUMP_SERVICE_PERMISSIONS = 16777216;
    public static final int DUMP_SERVICE_RESOLVERS = 8;
    public static final int DUMP_SHARED_USERS = 256;
    public static final int DUMP_SNAPSHOT_STATISTICS = 536870912;
    public static final int DUMP_VERIFIERS = 2048;
    public static final int DUMP_VERSION = 32768;
    public static final int DUMP_VOLUMES = 8388608;
    public static final int OPTION_DUMP_ALL_COMPONENTS = 2;
    public static final int OPTION_INCLUDE_APEX = 8;
    public static final int OPTION_SHOW_FILTERS = 1;
    public static final int OPTION_SKIP_PERMISSIONS = 4;
    private boolean mBrief;
    private boolean mCheckIn;
    private boolean mFullPreferred;
    private int mOptions;
    private SharedUserSetting mSharedUser;
    private String mTargetPackageName;
    private boolean mTitlePrinted;
    private int mTypes;

    public boolean isDumping(int i) {
        int i2 = this.mTypes;
        return (i2 == 0 && i != 8192) || (i2 & i) != 0;
    }

    public void setDump(int i) {
        this.mTypes = i | this.mTypes;
    }

    public boolean isOptionEnabled(int i) {
        return (this.mOptions & i) != 0;
    }

    public void setOptionEnabled(int i) {
        this.mOptions = i | this.mOptions;
    }

    public boolean onTitlePrinted() {
        boolean z = this.mTitlePrinted;
        this.mTitlePrinted = true;
        return z;
    }

    public boolean getTitlePrinted() {
        return this.mTitlePrinted;
    }

    public void setTitlePrinted(boolean z) {
        this.mTitlePrinted = z;
    }

    public SharedUserSetting getSharedUser() {
        return this.mSharedUser;
    }

    public void setSharedUser(SharedUserSetting sharedUserSetting) {
        this.mSharedUser = sharedUserSetting;
    }

    public String getTargetPackageName() {
        return this.mTargetPackageName;
    }

    public void setTargetPackageName(String str) {
        this.mTargetPackageName = str;
    }

    public boolean isFullPreferred() {
        return this.mFullPreferred;
    }

    public void setFullPreferred(boolean z) {
        this.mFullPreferred = z;
    }

    public boolean isCheckIn() {
        return this.mCheckIn;
    }

    public void setCheckIn(boolean z) {
        this.mCheckIn = z;
    }

    public boolean isBrief() {
        return this.mBrief;
    }

    public void setBrief(boolean z) {
        this.mBrief = z;
    }
}
