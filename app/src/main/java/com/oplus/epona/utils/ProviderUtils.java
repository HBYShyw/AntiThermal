package com.oplus.epona.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import com.oplus.epona.Constants;
import com.oplus.epona.ipc.cursor.ProviderCursor;

/* loaded from: classes.dex */
public class ProviderUtils {
    private static final String CONTENT_PREFIX = "content://";
    public static final String FIND_TRANSFER = "find_transfer";
    private static final String TAG = "ProviderUtils";

    public static Bundle findRemoteBundle(Context context, String str) {
        Bundle providerBundleFromQuery = getProviderBundleFromQuery(context, Uri.parse("content://com.oplus.appplatform.dispatcher/find_transfer/" + str));
        return providerBundleFromQuery == null ? getProviderBundleFromCall(context, str) : providerBundleFromQuery;
    }

    private static Bundle getProviderBundleFromCall(Context context, String str) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TRANSFER_KEY, str);
        try {
            return context.getContentResolver().call(Constants.DISPATCHER_PROVIDER_URI, Constants.FIND_TRANSFER, (String) null, bundle);
        } catch (Exception unused) {
            Logger.e(TAG, "failed to call provider: com.oplus.appplatform.dispatcher", new Object[0]);
            return null;
        }
    }

    private static Bundle getProviderBundleFromQuery(Context context, Uri uri) {
        try {
            Cursor query = context.getContentResolver().query(uri, null, null, null);
            try {
                if (query == null) {
                    Logger.e(TAG, "Get cursor null.", new Object[0]);
                    if (query != null) {
                        query.close();
                    }
                    return null;
                }
                Bundle stripBundle = ProviderCursor.stripBundle(query);
                query.close();
                return stripBundle;
            } finally {
            }
        } catch (Exception e10) {
            Logger.e(TAG, "Get cursor Exception : " + e10, new Object[0]);
            e10.printStackTrace();
            return null;
        }
    }
}
