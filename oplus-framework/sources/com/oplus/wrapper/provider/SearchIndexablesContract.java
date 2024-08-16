package com.oplus.wrapper.provider;

/* loaded from: classes.dex */
public class SearchIndexablesContract {
    public static final int COLUMN_INDEX_RAW_RANK = getColumnIndexRawRank();
    public static final int COLUMN_INDEX_RAW_TITLE = getColumnIndexRawTitle();
    public static final int COLUMN_INDEX_RAW_SUMMARY_ON = getColumnIndexRawSummaryOn();
    public static final int COLUMN_INDEX_RAW_SUMMARY_OFF = getColumnIndexRawSummaryOff();
    public static final int COLUMN_INDEX_RAW_ENTRIES = getColumnIndexRawEntries();
    public static final int COLUMN_INDEX_RAW_KEYWORDS = getColumnIndexRawKeywords();
    public static final int COLUMN_INDEX_RAW_SCREEN_TITLE = getColumnIndexRawScreenTitle();
    public static final int COLUMN_INDEX_RAW_CLASS_NAME = getColumnIndexRawClassName();
    public static final int COLUMN_INDEX_RAW_ICON_RESID = getColumnIndexRawIconResid();
    public static final int COLUMN_INDEX_RAW_INTENT_ACTION = getColumnIndexRawIntentAction();
    public static final int COLUMN_INDEX_RAW_INTENT_TARGET_PACKAGE = getColumnIndexRawIntentTargetPackage();
    public static final int COLUMN_INDEX_RAW_INTENT_TARGET_CLASS = getColumnIndexRawIntentTargetClass();
    public static final int COLUMN_INDEX_RAW_KEY = getColumnIndexRawKey();
    public static final int COLUMN_INDEX_RAW_USER_ID = getColumnIndexRawUserId();
    public static final String[] INDEXABLES_RAW_COLUMNS = getIndexblesRawColums();

    private SearchIndexablesContract() {
    }

    private static String[] getIndexblesRawColums() {
        return android.provider.SearchIndexablesContract.INDEXABLES_RAW_COLUMNS;
    }

    private static int getColumnIndexRawRank() {
        return 0;
    }

    private static int getColumnIndexRawTitle() {
        return 1;
    }

    private static int getColumnIndexRawSummaryOn() {
        return 2;
    }

    private static int getColumnIndexRawSummaryOff() {
        return 3;
    }

    private static int getColumnIndexRawEntries() {
        return 4;
    }

    private static int getColumnIndexRawKeywords() {
        return 5;
    }

    private static int getColumnIndexRawScreenTitle() {
        return 6;
    }

    private static int getColumnIndexRawClassName() {
        return 7;
    }

    private static int getColumnIndexRawIconResid() {
        return 8;
    }

    private static int getColumnIndexRawIntentAction() {
        return 9;
    }

    private static int getColumnIndexRawIntentTargetPackage() {
        return 10;
    }

    private static int getColumnIndexRawIntentTargetClass() {
        return 11;
    }

    private static int getColumnIndexRawKey() {
        return 12;
    }

    private static int getColumnIndexRawUserId() {
        return 13;
    }
}
