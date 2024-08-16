package com.oplus.statistics.util;

import android.content.Intent;
import com.oplus.statistics.util.IntentUtils;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class IntentUtils {
    private static final String TAG = "IntentUtils";

    public static boolean getBooleanExtra(Intent intent, String str, boolean z10) {
        try {
            return intent.getBooleanExtra(str, z10);
        } catch (Exception e10) {
            LogUtil.e(TAG, new Supplier() { // from class: ga.d
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$getBooleanExtra$0;
                    lambda$getBooleanExtra$0 = IntentUtils.lambda$getBooleanExtra$0(e10);
                    return lambda$getBooleanExtra$0;
                }
            });
            return z10;
        }
    }

    public static int getIntExtra(Intent intent, String str, int i10) {
        try {
            return intent.getIntExtra(str, i10);
        } catch (Exception e10) {
            LogUtil.e(TAG, new Supplier() { // from class: ga.f
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$getIntExtra$2;
                    lambda$getIntExtra$2 = IntentUtils.lambda$getIntExtra$2(e10);
                    return lambda$getIntExtra$2;
                }
            });
            return i10;
        }
    }

    public static long getLongExtra(Intent intent, String str, long j10) {
        try {
            return intent.getLongExtra(str, j10);
        } catch (Exception e10) {
            LogUtil.e(TAG, new Supplier() { // from class: ga.g
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$getLongExtra$3;
                    lambda$getLongExtra$3 = IntentUtils.lambda$getLongExtra$3(e10);
                    return lambda$getLongExtra$3;
                }
            });
            return j10;
        }
    }

    public static ArrayList<String> getStringArrayListExtra(Intent intent, String str) {
        try {
            return intent.getStringArrayListExtra(str);
        } catch (Exception e10) {
            LogUtil.e(TAG, new Supplier() { // from class: ga.e
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$getStringArrayListExtra$4;
                    lambda$getStringArrayListExtra$4 = IntentUtils.lambda$getStringArrayListExtra$4(e10);
                    return lambda$getStringArrayListExtra$4;
                }
            });
            return null;
        }
    }

    public static String getStringExtra(Intent intent, String str) {
        try {
            return intent.getStringExtra(str);
        } catch (Exception e10) {
            LogUtil.e(TAG, new Supplier() { // from class: ga.c
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$getStringExtra$1;
                    lambda$getStringExtra$1 = IntentUtils.lambda$getStringExtra$1(e10);
                    return lambda$getStringExtra$1;
                }
            });
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$getBooleanExtra$0(Exception exc) {
        return "intent getBooleanExtra exception:" + exc;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$getIntExtra$2(Exception exc) {
        return "intent getIntExtra exception:" + exc;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$getLongExtra$3(Exception exc) {
        return "intent getLongExtra exception:" + exc;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$getStringArrayListExtra$4(Exception exc) {
        return "intent getStringArrayListExtra exception:" + exc;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$getStringExtra$1(Exception exc) {
        return "intent getStringExtra exception:" + exc;
    }
}
