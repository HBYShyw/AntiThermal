package com.oplus.backup.sdk.compat;

/* loaded from: classes.dex */
public class DataSizeUtils {
    private static final long ACCOUNT_FILE_SIZE = 620;
    private static final long BROWSER_FILE_SIZE = 350;
    private static final long CALENDAR_FILE_SIZE = 700;
    private static final long CALLRECORD_FILE_SIZE = 450;
    private static final long CLOCK_FILE_SIZE = 250;
    private static final long CONTACTS_BLACKLIST_FILE_SIZE = 170;
    private static final long CONTACTS_HEAD_SIZE = 30720;
    private static final long CONTACTS_TEXT_SIZE = 400;
    private static final long LAUNCHER_FILE_SIZE = 450560;
    private static final long MMS_FILE_SIZE = 256000;
    private static final long SMS_FILE_SIZE = 750;
    private static final long SYSTEM_SETTING_FILE_SIZE = 81920;
    private static final long WEATHER_FILE_SIZE = 320;

    public static long estimateSize(int i10, int i11) {
        long j10;
        if (i10 == 1) {
            return ((i11 / 2) * CONTACTS_HEAD_SIZE) + (i11 * CONTACTS_TEXT_SIZE);
        }
        if (i10 == 2) {
            j10 = SMS_FILE_SIZE;
        } else if (i10 == 4) {
            j10 = MMS_FILE_SIZE;
        } else if (i10 == 8) {
            j10 = CALENDAR_FILE_SIZE;
        } else if (i10 == 272) {
            j10 = CALLRECORD_FILE_SIZE;
        } else if (i10 == 288) {
            j10 = CLOCK_FILE_SIZE;
        } else if (i10 == 304) {
            j10 = BROWSER_FILE_SIZE;
        } else if (i10 == 320) {
            j10 = WEATHER_FILE_SIZE;
        } else {
            if (i10 == 336) {
                return ACCOUNT_FILE_SIZE;
            }
            if (i10 == 352) {
                return LAUNCHER_FILE_SIZE;
            }
            if (i10 == 384) {
                return SYSTEM_SETTING_FILE_SIZE;
            }
            if (i10 != 592) {
                return 0L;
            }
            j10 = CONTACTS_BLACKLIST_FILE_SIZE;
        }
        return i11 * j10;
    }
}
