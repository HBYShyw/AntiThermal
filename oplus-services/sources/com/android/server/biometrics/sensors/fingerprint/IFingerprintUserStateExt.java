package com.android.server.biometrics.sensors.fingerprint;

import android.hardware.fingerprint.Fingerprint;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IFingerprintUserStateExt {
    default void attributeFingerprint(TypedXmlSerializer typedXmlSerializer, Fingerprint fingerprint) {
    }

    default Fingerprint getCopyFingerprint(Fingerprint fingerprint) {
        return fingerprint;
    }

    default Fingerprint parseBiometricsLocked(TypedXmlPullParser typedXmlPullParser, Fingerprint fingerprint) {
        return fingerprint;
    }

    default int setFingerprintFlags(int i, int i2) {
        return 0;
    }
}
