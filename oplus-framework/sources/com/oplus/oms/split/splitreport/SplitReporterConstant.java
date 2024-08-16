package com.oplus.oms.split.splitreport;

/* loaded from: classes.dex */
public class SplitReporterConstant {
    public static final String DOWNLOAD_QUERY_TAG = "download_query";
    public static final String DOWNLOAD_TAG = "download";
    public static final String INSTALL_TAG = "install";
    public static final String LOAD_TAG = "load";
    public static final String UNLOAD_TAG = "unload";
    public static final String UPGRADE_TAG = "upgrade";

    private SplitReporterConstant() {
    }

    /* loaded from: classes.dex */
    public static class SplitInstallCode {
        public static final String ACTION_PART_INSTALL = "pInstall";
        public static final String ACTION_TOTAL_INSTALL = "tInstall";
        public static final int RESULT_INSTALL_COPY_FAIL = 2;
        public static final int RESULT_INSTALL_SUCCESS = 1;

        private SplitInstallCode() {
        }
    }

    /* loaded from: classes.dex */
    public static class SplitDownLoadQueryCode {
        public static final int RESULT_NETWORK_UNAVAILABLE = -10;
        public static final int RESULT_POST_RESULT_DATA_EMPTY = -16;
        public static final int RESULT_POST_RESULT_FAIL = -15;
        public static final int RESULT_POST_RESULT_SYNTAX_FAIL = -14;
        public static final int RESULT_QUERY_SUCCESS = 1;
        public static final int RESULT_REQUEST_FAILURE = -11;
        public static final int RESULT_RESPONSE_BODY_ERROR = -13;
        public static final int RESULT_RESPONSE_FAILURE = -12;

        private SplitDownLoadQueryCode() {
        }
    }

    /* loaded from: classes.dex */
    public static class SplitDownLoadCode {
        public static final int RESULT_DOWNLOAD_CANCEL = -34;
        public static final int RESULT_DOWNLOAD_INTERNAL_ERROR = -31;
        public static final int RESULT_DOWNLOAD_NO_FREE_SPACE = -32;
        public static final int RESULT_DOWNLOAD_SAME_VERSION = -30;
        public static final int RESULT_DOWNLOAD_SUCCESS = 1;
        public static final int RESULT_DOWNLOAD_URL_ALL_UNAVAILABLE = -33;
        public static final String TYPE_DEFAULT = "0";
        public static final String TYPE_HTTP = "1";

        private SplitDownLoadCode() {
        }
    }

    /* loaded from: classes.dex */
    public static class SplitLoadCode {
        public static final String ACTION_PART_LOAD = "pload";
        public static final String ACTION_TOTAL_LOAD = "tload";
        public static final int RESULT_CLASS_LOAD_FAIL = 3;
        public static final int RESULT_COMPONENT_ACTIVE_FAIL = 6;
        public static final int RESULT_COMPONENT_FIND_FAIL = 5;
        public static final int RESULT_HAS_LOADED = 2;
        public static final int RESULT_LOADED_SUCCESS = 1;
        public static final int RESULT_RES_LOAD_FAIL = 4;

        private SplitLoadCode() {
        }
    }

    /* loaded from: classes.dex */
    public static class SplitUnLoadCode {
        public static final int RESULT_REMOVE_CLASS_DEX_ERROR = -50;
        public static final int RESULT_UNLOAD_INTERNAL_ERROR = -51;
        public static final int RESULT_UNLOAD_ON_TERMINATE_ERROR = -52;
        public static final int RESULT_UNLOAD_SUCCESS = 1;

        private SplitUnLoadCode() {
        }
    }

    /* loaded from: classes.dex */
    public static class SplitUpgradeCode {
        public static final int RESULT_CHECK_SPLIT_SIGN_ERROR = -72;
        public static final int RESULT_COPY_FROM_ERROR = -70;
        public static final int RESULT_COPY_SPLIT_INTERNAL_ERROR = -73;
        public static final int RESULT_COPY_SPLIT_IO_ERROR = -71;
        public static final int RESULT_COPY_SUCCESS = 1;
        public static final int RESULT_UPGRADE_SUCCESS = 2;

        private SplitUpgradeCode() {
        }
    }
}
