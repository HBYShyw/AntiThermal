package com.oplus.wrapper.provider;

import android.net.Uri;
import android.provider.Downloads;
import android.provider.oplus.Telephony;

/* loaded from: classes.dex */
public class Downloads {

    /* loaded from: classes.dex */
    public static final class Impl {
        public static final String ACTION_DOWNLOAD_COMPLETED = getActionDownloadCompleted();
        public static final String COLUMN_URI = getColumnUri();
        public static final String COLUMN_APP_DATA = getColumnAppData();
        public static final String COLUMN_FILE_NAME_HINT = getColumnFileNameHint();
        public static final String _DATA = getData();
        public static final String COLUMN_MIME_TYPE = getColumnMimeType();
        public static final String COLUMN_DESTINATION = getColumnDestination();
        public static final String COLUMN_VISIBILITY = getColumnVisibility();
        public static final String COLUMN_NOTIFICATION_PACKAGE = getColumnNotificationPackage();
        public static final String COLUMN_NOTIFICATION_CLASS = getColumnNotificationClass();
        public static final String COLUMN_REFERER = getColumnReferer();
        public static final String COLUMN_TITLE = getColumnTitle();
        public static final String COLUMN_DESCRIPTION = getColumnDescription();
        public static final int DESTINATION_FILE_URI = getDestinationFileUri();
        public static final int VISIBILITY_VISIBLE_NOTIFY_COMPLETED = getVisibilityVisibleNotifyCompleted();
        public static final Uri CONTENT_URI = getContentUri();
        public static final String _COUNT = getCount();
        public static final String _ID = getId();
        public static final String COLUMN_STATUS = getColumnStatus();

        private static String getActionDownloadCompleted() {
            return "android.intent.action.DOWNLOAD_COMPLETED";
        }

        private static String getColumnUri() {
            return "uri";
        }

        private static String getColumnAppData() {
            return "entity";
        }

        private static String getColumnFileNameHint() {
            return "hint";
        }

        private static String getData() {
            return Telephony.Mms.Part._DATA;
        }

        private static String getColumnMimeType() {
            return "mimetype";
        }

        private static String getColumnDestination() {
            return "destination";
        }

        private static String getColumnVisibility() {
            return "visibility";
        }

        private static String getColumnNotificationPackage() {
            return "notificationpackage";
        }

        private static String getColumnNotificationClass() {
            return "notificationclass";
        }

        private static String getColumnReferer() {
            return "referer";
        }

        private static String getColumnTitle() {
            return "title";
        }

        private static String getColumnDescription() {
            return "description";
        }

        private static int getDestinationFileUri() {
            return 4;
        }

        private static int getVisibilityVisibleNotifyCompleted() {
            return 1;
        }

        private static Uri getContentUri() {
            return Downloads.Impl.CONTENT_URI;
        }

        private static String getCount() {
            return "_count";
        }

        private static String getId() {
            return "_id";
        }

        private static String getColumnStatus() {
            return "status";
        }

        public static boolean isStatusSuccess(int status) {
            return Downloads.Impl.isStatusSuccess(status);
        }

        public static boolean isStatusCompleted(int status) {
            return Downloads.Impl.isStatusCompleted(status);
        }
    }
}
