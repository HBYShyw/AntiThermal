package com.android.server.biometrics.sensors.fingerprint;

import android.R;
import android.content.Context;
import android.hardware.fingerprint.Fingerprint;
import com.android.internal.annotations.GuardedBy;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.biometrics.sensors.BiometricUserState;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FingerprintUserState extends BiometricUserState<Fingerprint> {
    private static final String ATTR_DEVICE_ID = "deviceId";
    private static final String ATTR_FINGER_ID = "fingerId";
    private static final String ATTR_GROUP_ID = "groupId";
    private static final String ATTR_NAME = "name";
    private static final String TAG = "FingerprintState";
    private static final String TAG_FINGERPRINT = "fingerprint";
    private static final String TAG_FINGERPRINTS = "fingerprints";
    public IFingerprintUserStateExt mFingerprintUserStateExt;

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected String getBiometricsTag() {
        return TAG_FINGERPRINTS;
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected int getNameTemplateResource() {
        return R.string.kg_login_too_many_attempts;
    }

    private void initUserStateExt() {
        if (this.mFingerprintUserStateExt == null) {
            this.mFingerprintUserStateExt = (IFingerprintUserStateExt) ExtLoader.type(IFingerprintUserStateExt.class).base(this).create();
        }
    }

    public FingerprintUserState(Context context, int i, String str) {
        super(context, i, str);
        this.mFingerprintUserStateExt = null;
        initUserStateExt();
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected ArrayList<Fingerprint> getCopy(ArrayList<Fingerprint> arrayList) {
        ArrayList<Fingerprint> arrayList2 = new ArrayList<>();
        Iterator<Fingerprint> it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(this.mFingerprintUserStateExt.getCopyFingerprint(it.next()));
        }
        return arrayList2;
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected void doWriteState(TypedXmlSerializer typedXmlSerializer) throws Exception {
        ArrayList<Fingerprint> copy;
        synchronized (this) {
            copy = getCopy(this.mBiometrics);
        }
        typedXmlSerializer.startTag((String) null, TAG_FINGERPRINTS);
        int size = copy.size();
        for (int i = 0; i < size; i++) {
            Fingerprint fingerprint = copy.get(i);
            typedXmlSerializer.startTag((String) null, TAG_FINGERPRINT);
            typedXmlSerializer.attributeInt((String) null, ATTR_FINGER_ID, fingerprint.getBiometricId());
            typedXmlSerializer.attribute((String) null, ATTR_NAME, fingerprint.getName().toString());
            typedXmlSerializer.attributeInt((String) null, ATTR_GROUP_ID, fingerprint.getGroupId());
            typedXmlSerializer.attributeLong((String) null, ATTR_DEVICE_ID, fingerprint.getDeviceId());
            this.mFingerprintUserStateExt.attributeFingerprint(typedXmlSerializer, fingerprint);
            typedXmlSerializer.endTag((String) null, TAG_FINGERPRINT);
        }
        typedXmlSerializer.endTag((String) null, TAG_FINGERPRINTS);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    @GuardedBy({"this"})
    protected void parseBiometricsLocked(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException {
        int depth = typedXmlPullParser.getDepth();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1) {
                return;
            }
            if (next == 3 && typedXmlPullParser.getDepth() <= depth) {
                return;
            }
            if (next != 3 && next != 4 && typedXmlPullParser.getName().equals(TAG_FINGERPRINT)) {
                String attributeValue = typedXmlPullParser.getAttributeValue((String) null, ATTR_NAME);
                int attributeInt = typedXmlPullParser.getAttributeInt((String) null, ATTR_GROUP_ID);
                int attributeInt2 = typedXmlPullParser.getAttributeInt((String) null, ATTR_FINGER_ID);
                long attributeLong = typedXmlPullParser.getAttributeLong((String) null, ATTR_DEVICE_ID);
                initUserStateExt();
                this.mBiometrics.add(this.mFingerprintUserStateExt.parseBiometricsLocked(typedXmlPullParser, new Fingerprint(attributeValue, attributeInt, attributeInt2, attributeLong)));
            }
        }
    }
}
