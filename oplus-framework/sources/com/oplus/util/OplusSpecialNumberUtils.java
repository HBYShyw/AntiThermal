package com.oplus.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import java.io.InputStream;

@Deprecated
/* loaded from: classes.dex */
public class OplusSpecialNumberUtils {

    @Deprecated
    public static final String SpecialNumberTable = "special_contacts";

    @Deprecated
    public static final Uri SPECIAL_NUMBER_CONTENT_URI = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, SpecialNumberTable);

    @Deprecated
    /* loaded from: classes.dex */
    public static class OplusSpecialNumColumns {

        @Deprecated
        public static final String CN_NAME = "cn_name";

        @Deprecated
        public static final String EN_NAME = "en_name";

        @Deprecated
        public static final String NUMBER = "number";

        @Deprecated
        public static final String OPLUS_URL = "oplus_url";

        @Deprecated
        public static final String PHOTO_DATA = "photo_data";

        @Deprecated
        public static final String TW_NAME = "tw_name";

        @Deprecated
        public static final String _ID = "_id";
    }

    @Deprecated
    public OplusSpecialNumberUtils(Context context) {
        throw new UnsupportedOperationException("OplusSpecialNumberUtils is deprecated, no longer supported.");
    }

    @Deprecated
    public boolean numberNeedSpecialHandle(String number) {
        throw new UnsupportedOperationException("OplusSpecialNumberUtils is deprecated, no longer supported.");
    }

    @Deprecated
    public boolean isSpecialNumber(String number) {
        throw new UnsupportedOperationException("OplusSpecialNumberUtils is deprecated, no longer supported.");
    }

    @Deprecated
    public boolean isNumberStoredInContacts(String number) {
        throw new UnsupportedOperationException("OplusSpecialNumberUtils is deprecated, no longer supported.");
    }

    @Deprecated
    public String getNameOfnumber() {
        throw new UnsupportedOperationException("OplusSpecialNumberUtils is deprecated, no longer supported.");
    }

    @Deprecated
    public InputStream getInputStreamImageOfnumber() {
        throw new UnsupportedOperationException("OplusSpecialNumberUtils is deprecated, no longer supported.");
    }

    @Deprecated
    public Drawable getImageOfnumber() {
        throw new UnsupportedOperationException("OplusSpecialNumberUtils is deprecated, no longer supported.");
    }
}
