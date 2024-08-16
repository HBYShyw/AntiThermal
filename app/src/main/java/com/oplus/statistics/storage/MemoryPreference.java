package com.oplus.statistics.storage;

import android.text.TextUtils;
import com.oplus.statistics.storage.MemoryPreference;
import com.oplus.statistics.util.LogUtil;
import com.oplus.statistics.util.Supplier;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class MemoryPreference {
    private static final String TAG = "MemoryPreference";
    private Map<String, String> mMemoryPref = new HashMap();

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$getInt$1(String str, String str2, NumberFormatException numberFormatException) {
        return "getInt key=" + str + ", value=" + str2 + ", exception=" + numberFormatException.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$getLong$0(String str, String str2, NumberFormatException numberFormatException) {
        return "getLong key=" + str + ", value=" + str2 + ", exception=" + numberFormatException.toString();
    }

    public int getInt(final String str, int i10) {
        final String str2 = this.mMemoryPref.get(str);
        if (TextUtils.isEmpty(str2)) {
            return i10;
        }
        try {
            return Integer.parseInt(str2);
        } catch (NumberFormatException e10) {
            LogUtil.w(TAG, new Supplier() { // from class: fa.b
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$getInt$1;
                    lambda$getInt$1 = MemoryPreference.lambda$getInt$1(str, str2, e10);
                    return lambda$getInt$1;
                }
            });
            return i10;
        }
    }

    public long getLong(final String str, long j10) {
        final String str2 = this.mMemoryPref.get(str);
        if (TextUtils.isEmpty(str2)) {
            return j10;
        }
        try {
            return Long.parseLong(str2);
        } catch (NumberFormatException e10) {
            LogUtil.w(TAG, new Supplier() { // from class: fa.a
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$getLong$0;
                    lambda$getLong$0 = MemoryPreference.lambda$getLong$0(str, str2, e10);
                    return lambda$getLong$0;
                }
            });
            return j10;
        }
    }

    public String getString(String str, String str2) {
        String str3 = this.mMemoryPref.get(str);
        return TextUtils.isEmpty(str3) ? str2 : str3;
    }

    public void setInt(String str, long j10) {
        this.mMemoryPref.put(str, String.valueOf(j10));
    }

    public void setLong(String str, long j10) {
        this.mMemoryPref.put(str, String.valueOf(j10));
    }

    public void setString(String str, String str2) {
        this.mMemoryPref.put(str, str2);
    }
}
