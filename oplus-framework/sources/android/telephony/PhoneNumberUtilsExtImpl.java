package android.telephony;

import android.app.OplusUxIconConstants;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import com.oplus.util.OplusResolverIntentUtil;

/* loaded from: classes.dex */
public class PhoneNumberUtilsExtImpl implements IPhoneNumberUtilsExt {
    private static final String IP_CALL = "ip_call";
    private static final String IP_CALL_PREFIX = "ip_call_prefix_sub";
    static final String LOG_TAG = "PhoneNumberUtilsExtImpl";
    public static final char PAUSE = ',';
    public static final char WAIT = ';';
    public static final char WILD = 'N';

    public PhoneNumberUtilsExtImpl() {
    }

    public PhoneNumberUtilsExtImpl(Object base) {
    }

    public static String checkAndAppendPrefix(Intent intent, int subscription, String number, Context context) {
        boolean isIPPrefix = intent.getBooleanExtra(IP_CALL, false);
        if (isIPPrefix && number != null && subscription < TelephonyManager.getDefault().getPhoneCount()) {
            String IPPrefix = Settings.System.getString(context.getContentResolver(), IP_CALL_PREFIX + (subscription + 1));
            if (!TextUtils.isEmpty(IPPrefix)) {
                return IPPrefix + number;
            }
        }
        return number;
    }

    public String getNumberFromIntent(Intent intent, Context context, String oriNumber) {
        int subscription;
        int[] subId;
        String number = null;
        Uri uri = intent.getData();
        if (uri == null) {
            return null;
        }
        String scheme = uri.getScheme();
        if (TelephonyManager.getDefault().isMultiSimEnabled()) {
            int subscription2 = intent.getIntExtra(OplusTelephonyConstant.SUBSCRIPTION_KEY, SubscriptionManager.getDefaultVoicePhoneId());
            subscription = subscription2;
        } else {
            subscription = 0;
        }
        if (scheme.equals("tel") || scheme.equals("sip")) {
            return checkAndAppendPrefix(intent, subscription, uri.getSchemeSpecificPart(), context);
        }
        if (scheme.equals("voicemail")) {
            return (!TelephonyManager.getDefault().isMultiSimEnabled() || (subId = SubscriptionManager.getSubId(subscription)) == null || subId[0] <= 0) ? TelephonyManager.getDefault().getVoiceMailNumber() : TelephonyManager.getDefault().getVoiceMailNumber(subId[0]);
        }
        if (context == null) {
            return null;
        }
        intent.resolveType(context);
        String authority = uri.getAuthority();
        String phoneColumn = OplusResolverIntentUtil.DEFAULT_APP_CONTACTS.equals(authority) ? "number" : OplusUxIconConstants.IconLoader.COM_ANDROID_CONTACTS.equals(authority) ? "data1" : null;
        Cursor c = null;
        try {
            try {
                Cursor c2 = context.getContentResolver().query(uri, new String[]{phoneColumn}, null, null, null);
                if (c2 != null) {
                    try {
                        if (c2.moveToFirst()) {
                            number = c2.getString(c2.getColumnIndex(phoneColumn));
                        }
                    } catch (RuntimeException e) {
                        e = e;
                        c = c2;
                        Rlog.e(LOG_TAG, "Error getting phone number.", e);
                        if (c != null) {
                            c.close();
                        }
                        return checkAndAppendPrefix(intent, subscription, number, context);
                    } catch (Throwable th) {
                        th = th;
                        c = c2;
                        if (c != null) {
                            c.close();
                        }
                        throw th;
                    }
                }
                if (c2 != null) {
                    c2.close();
                }
            } catch (RuntimeException e2) {
                e = e2;
            }
            return checkAndAppendPrefix(intent, subscription, number, context);
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public String stripSeparators(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        int len = phoneNumber.length();
        StringBuilder ret = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = phoneNumber.charAt(i);
            if (PhoneNumberUtils.isNonSeparator(c)) {
                ret.append(c);
            }
        }
        return ret.toString();
    }
}
