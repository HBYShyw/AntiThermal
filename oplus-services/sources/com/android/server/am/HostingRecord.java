package com.android.server.am;

import android.content.ComponentName;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class HostingRecord {
    private static final int APP_ZYGOTE = 2;
    public static final String HOSTING_TYPE_ACTIVITY = "activity";
    public static final String HOSTING_TYPE_ADDED_APPLICATION = "added application";
    public static final String HOSTING_TYPE_BACKUP = "backup";
    public static final String HOSTING_TYPE_BROADCAST = "broadcast";
    public static final String HOSTING_TYPE_CONTENT_PROVIDER = "content provider";
    public static final String HOSTING_TYPE_EMPTY = "";
    public static final String HOSTING_TYPE_LINK_FAIL = "link fail";
    public static final String HOSTING_TYPE_NEXT_ACTIVITY = "next-activity";
    public static final String HOSTING_TYPE_NEXT_TOP_ACTIVITY = "next-top-activity";
    public static final String HOSTING_TYPE_ON_HOLD = "on-hold";
    public static final String HOSTING_TYPE_RESTART = "restart";
    public static final String HOSTING_TYPE_SERVICE = "service";
    public static final String HOSTING_TYPE_SYSTEM = "system";
    public static final String HOSTING_TYPE_TOP_ACTIVITY = "top-activity";
    private static final int REGULAR_ZYGOTE = 0;
    public static final String TRIGGER_TYPE_ALARM = "alarm";
    public static final String TRIGGER_TYPE_JOB = "job";
    public static final String TRIGGER_TYPE_PUSH_MESSAGE = "push_message";
    public static final String TRIGGER_TYPE_PUSH_MESSAGE_OVER_QUOTA = "push_message_over_quota";
    public static final String TRIGGER_TYPE_UNKNOWN = "unknown";
    private static final int WEBVIEW_ZYGOTE = 1;
    private final String mAction;
    private final String mDefiningPackageName;
    private final String mDefiningProcessName;
    private final int mDefiningUid;
    private final String mHostingName;
    private IHostingRecordExt mHostingRecordExt;
    private final String mHostingType;
    private final int mHostingZygote;
    private final boolean mIsTopApp;
    private final String mTriggerType;
    private HostingRecordWrapper mWrapper;

    public IHostingRecordWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class HostingRecordWrapper implements IHostingRecordWrapper {
        private HostingRecordWrapper() {
        }

        @Override // com.android.server.am.IHostingRecordWrapper
        public IHostingRecordExt getExtImpl() {
            return HostingRecord.this.mHostingRecordExt;
        }
    }

    public HostingRecord(String str) {
        this(str, null, 0, null, -1, false, null, null, "unknown");
    }

    public HostingRecord(String str, ComponentName componentName) {
        this(str, componentName, 0);
    }

    public HostingRecord(String str, ComponentName componentName, String str2, String str3) {
        this(str, componentName.toShortString(), 0, null, -1, false, null, str2, str3);
    }

    public HostingRecord(String str, ComponentName componentName, String str2, int i, String str3, String str4) {
        this(str, componentName.toShortString(), 0, str2, i, false, str3, null, str4);
    }

    public HostingRecord(String str, ComponentName componentName, boolean z) {
        this(str, componentName.toShortString(), 0, null, -1, z, null, null, "unknown");
    }

    public HostingRecord(String str, String str2) {
        this(str, str2, 0);
    }

    private HostingRecord(String str, ComponentName componentName, int i) {
        this(str, componentName.toShortString(), i);
    }

    private HostingRecord(String str, String str2, int i) {
        this(str, str2, i, null, -1, false, null, null, "unknown");
    }

    private HostingRecord(String str, String str2, int i, String str3, int i2, boolean z, String str4, String str5, String str6) {
        this.mWrapper = new HostingRecordWrapper();
        this.mHostingRecordExt = (IHostingRecordExt) ExtLoader.type(IHostingRecordExt.class).base(this).create();
        this.mHostingType = str;
        this.mHostingName = str2;
        this.mHostingZygote = i;
        this.mDefiningPackageName = str3;
        this.mDefiningUid = i2;
        this.mIsTopApp = z;
        this.mDefiningProcessName = str4;
        this.mAction = str5;
        this.mTriggerType = str6;
    }

    public String getType() {
        return this.mHostingType;
    }

    public String getName() {
        return this.mHostingName;
    }

    public boolean isTopApp() {
        return this.mIsTopApp;
    }

    public int getDefiningUid() {
        return this.mDefiningUid;
    }

    public String getDefiningPackageName() {
        return this.mDefiningPackageName;
    }

    public String getDefiningProcessName() {
        return this.mDefiningProcessName;
    }

    public String getAction() {
        return this.mAction;
    }

    public String getTriggerType() {
        return this.mTriggerType;
    }

    public static HostingRecord byWebviewZygote(ComponentName componentName, String str, int i, String str2) {
        return new HostingRecord("", componentName.toShortString(), 1, str, i, false, str2, null, "unknown");
    }

    public static HostingRecord byAppZygote(ComponentName componentName, String str, int i, String str2) {
        return new HostingRecord("", componentName.toShortString(), 2, str, i, false, str2, null, "unknown");
    }

    public boolean usesAppZygote() {
        return this.mHostingZygote == 2;
    }

    public boolean usesWebviewZygote() {
        return this.mHostingZygote == 1;
    }

    public static int getHostingTypeIdStatsd(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1726126969:
                if (str.equals(HOSTING_TYPE_TOP_ACTIVITY)) {
                    c = 0;
                    break;
                }
                break;
            case -1682898044:
                if (str.equals(HOSTING_TYPE_LINK_FAIL)) {
                    c = 1;
                    break;
                }
                break;
            case -1655966961:
                if (str.equals(HOSTING_TYPE_ACTIVITY)) {
                    c = 2;
                    break;
                }
                break;
            case -1618876223:
                if (str.equals("broadcast")) {
                    c = 3;
                    break;
                }
                break;
            case -1526161119:
                if (str.equals(HOSTING_TYPE_NEXT_TOP_ACTIVITY)) {
                    c = 4;
                    break;
                }
                break;
            case -1396673086:
                if (str.equals(HOSTING_TYPE_BACKUP)) {
                    c = 5;
                    break;
                }
                break;
            case -1372333075:
                if (str.equals(HOSTING_TYPE_ON_HOLD)) {
                    c = 6;
                    break;
                }
                break;
            case -1355707223:
                if (str.equals(HOSTING_TYPE_NEXT_ACTIVITY)) {
                    c = 7;
                    break;
                }
                break;
            case -887328209:
                if (str.equals("system")) {
                    c = '\b';
                    break;
                }
                break;
            case 0:
                if (str.equals("")) {
                    c = '\t';
                    break;
                }
                break;
            case 1097506319:
                if (str.equals(HOSTING_TYPE_RESTART)) {
                    c = '\n';
                    break;
                }
                break;
            case 1418439096:
                if (str.equals(HOSTING_TYPE_CONTENT_PROVIDER)) {
                    c = 11;
                    break;
                }
                break;
            case 1637159472:
                if (str.equals(HOSTING_TYPE_ADDED_APPLICATION)) {
                    c = '\f';
                    break;
                }
                break;
            case 1984153269:
                if (str.equals(HOSTING_TYPE_SERVICE)) {
                    c = '\r';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 13;
            case 1:
                return 6;
            case 2:
                return 1;
            case 3:
                return 4;
            case 4:
                return 9;
            case 5:
                return 3;
            case 6:
                return 7;
            case 7:
                return 8;
            case '\b':
                return 12;
            case '\t':
                return 14;
            case '\n':
                return 10;
            case 11:
                return 5;
            case '\f':
                return 2;
            case '\r':
                return 11;
            default:
                return 0;
        }
    }

    public static int getTriggerTypeForStatsd(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -2000959542:
                if (str.equals(TRIGGER_TYPE_PUSH_MESSAGE_OVER_QUOTA)) {
                    c = 0;
                    break;
                }
                break;
            case 105405:
                if (str.equals(TRIGGER_TYPE_JOB)) {
                    c = 1;
                    break;
                }
                break;
            case 92895825:
                if (str.equals(TRIGGER_TYPE_ALARM)) {
                    c = 2;
                    break;
                }
                break;
            case 679713762:
                if (str.equals(TRIGGER_TYPE_PUSH_MESSAGE)) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 3;
            case 1:
                return 4;
            case 2:
                return 1;
            case 3:
                return 2;
            default:
                return 0;
        }
    }
}
