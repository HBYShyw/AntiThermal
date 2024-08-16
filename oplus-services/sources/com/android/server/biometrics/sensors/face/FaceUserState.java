package com.android.server.biometrics.sensors.face;

import android.R;
import android.content.Context;
import android.hardware.face.Face;
import com.android.internal.annotations.GuardedBy;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.biometrics.sensors.BiometricUserState;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FaceUserState extends BiometricUserState<Face> {
    private static final String ATTR_DEVICE_ID = "deviceId";
    private static final String ATTR_FACE_ID = "faceId";
    private static final String ATTR_NAME = "name";
    private static final String TAG = "FaceState";
    private static final String TAG_FACE = "face";
    private static final String TAG_FACES = "faces";

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected String getBiometricsTag() {
        return TAG_FACES;
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected int getNameTemplateResource() {
        return R.string.js_dialog_title;
    }

    public FaceUserState(Context context, int i, String str) {
        super(context, i, str);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected ArrayList<Face> getCopy(ArrayList<Face> arrayList) {
        ArrayList<Face> arrayList2 = new ArrayList<>();
        Iterator<Face> it = arrayList.iterator();
        while (it.hasNext()) {
            Face next = it.next();
            arrayList2.add(new Face(next.getName(), next.getBiometricId(), next.getDeviceId()));
        }
        return arrayList2;
    }

    @Override // com.android.server.biometrics.sensors.BiometricUserState
    protected void doWriteState(TypedXmlSerializer typedXmlSerializer) throws Exception {
        ArrayList<Face> copy;
        synchronized (this) {
            copy = getCopy(this.mBiometrics);
        }
        typedXmlSerializer.startTag((String) null, TAG_FACES);
        int size = copy.size();
        for (int i = 0; i < size; i++) {
            Face face = copy.get(i);
            typedXmlSerializer.startTag((String) null, TAG_FACE);
            typedXmlSerializer.attributeInt((String) null, ATTR_FACE_ID, face.getBiometricId());
            typedXmlSerializer.attribute((String) null, ATTR_NAME, face.getName().toString());
            typedXmlSerializer.attributeLong((String) null, ATTR_DEVICE_ID, face.getDeviceId());
            typedXmlSerializer.endTag((String) null, TAG_FACE);
        }
        typedXmlSerializer.endTag((String) null, TAG_FACES);
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
            if (next != 3 && next != 4 && typedXmlPullParser.getName().equals(TAG_FACE)) {
                this.mBiometrics.add(new Face(typedXmlPullParser.getAttributeValue((String) null, ATTR_NAME), typedXmlPullParser.getAttributeInt((String) null, ATTR_FACE_ID), typedXmlPullParser.getAttributeLong((String) null, ATTR_DEVICE_ID)));
            }
        }
    }
}
