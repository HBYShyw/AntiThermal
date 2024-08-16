package com.android.server.net.watchlist;

import android.privacy.DifferentialPrivacyEncoder;
import android.privacy.internal.longitudinalreporting.LongitudinalReportingConfig;
import android.privacy.internal.longitudinalreporting.LongitudinalReportingEncoder;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.net.watchlist.WatchlistReportDbHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class PrivacyUtils {
    private static final boolean DEBUG = false;
    private static final String ENCODER_ID_PREFIX = "watchlist_encoder:";
    private static final double PROB_F = 0.469d;
    private static final double PROB_P = 0.28d;
    private static final double PROB_Q = 1.0d;
    private static final String TAG = "PrivacyUtils";

    private PrivacyUtils() {
    }

    @VisibleForTesting
    static DifferentialPrivacyEncoder createInsecureDPEncoderForTest(String str) {
        return LongitudinalReportingEncoder.createInsecureEncoderForTest(createLongitudinalReportingConfig(str));
    }

    @VisibleForTesting
    static DifferentialPrivacyEncoder createSecureDPEncoder(byte[] bArr, String str) {
        return LongitudinalReportingEncoder.createEncoder(createLongitudinalReportingConfig(str), bArr);
    }

    private static LongitudinalReportingConfig createLongitudinalReportingConfig(String str) {
        return new LongitudinalReportingConfig(ENCODER_ID_PREFIX + str, PROB_F, PROB_P, PROB_Q);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public static Map<String, Boolean> createDpEncodedReportMap(boolean z, byte[] bArr, List<String> list, WatchlistReportDbHelper.AggregatedResult aggregatedResult) {
        DifferentialPrivacyEncoder createInsecureDPEncoderForTest;
        int size = list.size();
        HashMap hashMap = new HashMap(size);
        for (int i = 0; i < size; i++) {
            String str = list.get(i);
            if (z) {
                createInsecureDPEncoderForTest = createSecureDPEncoder(bArr, str);
            } else {
                createInsecureDPEncoderForTest = createInsecureDPEncoderForTest(str);
            }
            boolean z2 = true;
            if ((createInsecureDPEncoderForTest.encodeBoolean(aggregatedResult.appDigestList.contains(str))[0] & 1) != 1) {
                z2 = false;
            }
            hashMap.put(str, Boolean.valueOf(z2));
        }
        return hashMap;
    }
}
