package com.android.server.sensorprivacy;

import android.os.Environment;
import android.util.ArrayMap;
import android.util.AtomicFile;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.util.XmlUtils;
import com.android.internal.util.dump.DualDumpOutputStream;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.IoThread;
import com.android.server.LocalServices;
import com.android.server.pm.UserManagerInternal;
import com.android.server.timezonedetector.ServiceConfigAccessor;
import com.android.server.voiceinteraction.DatabaseHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.BiConsumer;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PersistedState {
    private static final int CURRENT_PERSISTENCE_VERSION = 2;
    private static final int CURRENT_VERSION = 2;
    private static final String LOG_TAG = "PersistedState";
    private static final String XML_ATTRIBUTE_LAST_CHANGE = "last-change";
    private static final String XML_ATTRIBUTE_PERSISTENCE_VERSION = "persistence-version";
    private static final String XML_ATTRIBUTE_SENSOR = "sensor";
    private static final String XML_ATTRIBUTE_STATE_TYPE = "state-type";
    private static final String XML_ATTRIBUTE_TOGGLE_TYPE = "toggle-type";
    private static final String XML_ATTRIBUTE_USER_ID = "user-id";
    private static final String XML_ATTRIBUTE_VERSION = "version";
    private static final String XML_TAG_SENSOR_PRIVACY = "sensor-privacy";
    private static final String XML_TAG_SENSOR_STATE = "sensor-state";
    private final AtomicFile mAtomicFile;
    private ArrayMap<TypeUserSensor, SensorState> mStates = new ArrayMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PersistedState fromFile(String str) {
        return new PersistedState(str);
    }

    private PersistedState(String str) {
        this.mAtomicFile = new AtomicFile(new File(Environment.getDataSystemDirectory(), str));
        readState();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00de  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00e7  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00f1  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x005b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v15 */
    /* JADX WARN: Type inference failed for: r2v24 */
    /* JADX WARN: Type inference failed for: r2v25 */
    /* JADX WARN: Type inference failed for: r7v6, types: [com.android.server.sensorprivacy.PersistedState$PVersion1] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void readState() {
        Object obj;
        PVersion2 pVersion2;
        boolean z;
        boolean z2;
        PVersion2 pVersion22;
        AtomicFile atomicFile;
        IOException e;
        AtomicFile atomicFile2 = this.mAtomicFile;
        if (!atomicFile2.exists()) {
            AtomicFile atomicFile3 = new AtomicFile(new File(Environment.getDataSystemDirectory(), "sensor_privacy.xml"));
            if (atomicFile3.exists()) {
                try {
                    FileInputStream openRead = atomicFile3.openRead();
                    try {
                        XmlUtils.beginDocument(Xml.resolvePullParser(openRead), XML_TAG_SENSOR_PRIVACY);
                        if (openRead != null) {
                            try {
                                openRead.close();
                            } catch (IOException e2) {
                                e = e2;
                                atomicFile = atomicFile3;
                                Log.e(LOG_TAG, "Caught an exception reading the state from storage: ", e);
                                atomicFile3.delete();
                                atomicFile2 = atomicFile;
                                int i = 2;
                                boolean z3 = false;
                                boolean z4 = false;
                                boolean z5 = false;
                                if (atomicFile2.exists()) {
                                }
                                obj = null;
                                if (obj == null) {
                                }
                                z = obj instanceof PVersion0;
                                ?? r2 = obj;
                                if (z) {
                                }
                                z2 = r2 instanceof PVersion1;
                                pVersion22 = r2;
                                if (z2) {
                                }
                                if (!(pVersion22 instanceof PVersion2)) {
                                }
                            } catch (XmlPullParserException unused) {
                            }
                        }
                        atomicFile2 = atomicFile3;
                    } catch (Throwable th) {
                        if (openRead != null) {
                            try {
                                openRead.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                } catch (IOException e3) {
                    atomicFile = atomicFile2;
                    e = e3;
                    Log.e(LOG_TAG, "Caught an exception reading the state from storage: ", e);
                    atomicFile3.delete();
                    atomicFile2 = atomicFile;
                    int i2 = 2;
                    boolean z32 = false;
                    boolean z42 = false;
                    boolean z52 = false;
                    if (atomicFile2.exists()) {
                    }
                    obj = null;
                    if (obj == null) {
                    }
                    z = obj instanceof PVersion0;
                    ?? r22 = obj;
                    if (z) {
                    }
                    z2 = r22 instanceof PVersion1;
                    pVersion22 = r22;
                    if (z2) {
                    }
                    if (!(pVersion22 instanceof PVersion2)) {
                    }
                } catch (XmlPullParserException unused2) {
                }
            }
        }
        int i22 = 2;
        boolean z322 = false;
        boolean z422 = false;
        boolean z522 = false;
        if (atomicFile2.exists()) {
            try {
                FileInputStream openRead2 = atomicFile2.openRead();
                try {
                    TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(openRead2);
                    XmlUtils.beginDocument(resolvePullParser, XML_TAG_SENSOR_PRIVACY);
                    int i3 = 0;
                    int attributeInt = resolvePullParser.getAttributeInt((String) null, XML_ATTRIBUTE_PERSISTENCE_VERSION, 0);
                    if (attributeInt == 0) {
                        PVersion0 pVersion0 = new PVersion0(i3);
                        readPVersion0(resolvePullParser, pVersion0);
                        obj = pVersion0;
                    } else {
                        if (attributeInt == 1) {
                            ?? pVersion1 = new PVersion1(resolvePullParser.getAttributeInt((String) null, XML_ATTRIBUTE_VERSION, 1));
                            readPVersion1(resolvePullParser, pVersion1);
                            pVersion2 = pVersion1;
                        } else if (attributeInt == 2) {
                            PVersion2 pVersion23 = new PVersion2(resolvePullParser.getAttributeInt((String) null, XML_ATTRIBUTE_VERSION, 2));
                            readPVersion2(resolvePullParser, pVersion23);
                            pVersion2 = pVersion23;
                        } else {
                            Log.e(LOG_TAG, "Unknown persistence version: " + attributeInt + ". Deleting.", new RuntimeException());
                            atomicFile2.delete();
                            obj = null;
                        }
                        obj = pVersion2;
                    }
                    if (openRead2 != null) {
                        openRead2.close();
                    }
                } catch (Throwable th3) {
                    if (openRead2 != null) {
                        try {
                            openRead2.close();
                        } catch (Throwable th4) {
                            th3.addSuppressed(th4);
                        }
                    }
                    throw th3;
                }
            } catch (IOException | RuntimeException | XmlPullParserException e4) {
                Log.e(LOG_TAG, "Caught an exception reading the state from storage: ", e4);
                atomicFile2.delete();
            }
            if (obj == null) {
                obj = new PVersion2(i22);
            }
            z = obj instanceof PVersion0;
            ?? r222 = obj;
            if (z) {
                r222 = PVersion1.fromPVersion0((PVersion0) obj);
            }
            z2 = r222 instanceof PVersion1;
            pVersion22 = r222;
            if (z2) {
                pVersion22 = PVersion2.fromPVersion1((PVersion1) r222);
            }
            if (!(pVersion22 instanceof PVersion2)) {
                this.mStates = pVersion22.mStates;
                return;
            } else {
                Log.e(LOG_TAG, "State not successfully upgraded.");
                this.mStates = new ArrayMap<>();
                return;
            }
        }
        obj = null;
        if (obj == null) {
        }
        z = obj instanceof PVersion0;
        ?? r2222 = obj;
        if (z) {
        }
        z2 = r2222 instanceof PVersion1;
        pVersion22 = r2222;
        if (z2) {
        }
        if (!(pVersion22 instanceof PVersion2)) {
        }
    }

    private static void readPVersion0(TypedXmlPullParser typedXmlPullParser, PVersion0 pVersion0) throws XmlPullParserException, IOException {
        XmlUtils.nextElement(typedXmlPullParser);
        while (typedXmlPullParser.getEventType() != 1) {
            if ("individual-sensor-privacy".equals(typedXmlPullParser.getName())) {
                pVersion0.addState(XmlUtils.readIntAttribute(typedXmlPullParser, XML_ATTRIBUTE_SENSOR), XmlUtils.readBooleanAttribute(typedXmlPullParser, ServiceConfigAccessor.PROVIDER_MODE_ENABLED));
                XmlUtils.skipCurrentTag(typedXmlPullParser);
            } else {
                XmlUtils.nextElement(typedXmlPullParser);
            }
        }
    }

    private static void readPVersion1(TypedXmlPullParser typedXmlPullParser, PVersion1 pVersion1) throws XmlPullParserException, IOException {
        while (typedXmlPullParser.getEventType() != 1) {
            XmlUtils.nextElement(typedXmlPullParser);
            if ("user".equals(typedXmlPullParser.getName())) {
                int attributeInt = typedXmlPullParser.getAttributeInt((String) null, "id");
                int depth = typedXmlPullParser.getDepth();
                while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                    if ("individual-sensor-privacy".equals(typedXmlPullParser.getName())) {
                        pVersion1.addState(attributeInt, typedXmlPullParser.getAttributeInt((String) null, XML_ATTRIBUTE_SENSOR), typedXmlPullParser.getAttributeBoolean((String) null, ServiceConfigAccessor.PROVIDER_MODE_ENABLED));
                    }
                }
            }
        }
    }

    private static void readPVersion2(TypedXmlPullParser typedXmlPullParser, PVersion2 pVersion2) throws XmlPullParserException, IOException {
        while (typedXmlPullParser.getEventType() != 1) {
            XmlUtils.nextElement(typedXmlPullParser);
            if (XML_TAG_SENSOR_STATE.equals(typedXmlPullParser.getName())) {
                pVersion2.addState(typedXmlPullParser.getAttributeInt((String) null, XML_ATTRIBUTE_TOGGLE_TYPE), typedXmlPullParser.getAttributeInt((String) null, XML_ATTRIBUTE_USER_ID), typedXmlPullParser.getAttributeInt((String) null, XML_ATTRIBUTE_SENSOR), typedXmlPullParser.getAttributeInt((String) null, XML_ATTRIBUTE_STATE_TYPE), typedXmlPullParser.getAttributeLong((String) null, XML_ATTRIBUTE_LAST_CHANGE));
            } else {
                XmlUtils.skipCurrentTag(typedXmlPullParser);
            }
        }
    }

    public SensorState getState(int i, int i2, int i3) {
        return this.mStates.get(new TypeUserSensor(i, i2, i3));
    }

    public SensorState setState(int i, int i2, int i3, SensorState sensorState) {
        return this.mStates.put(new TypeUserSensor(i, i2, i3), sensorState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class TypeUserSensor {
        int mSensor;
        int mType;
        int mUserId;

        TypeUserSensor(int i, int i2, int i3) {
            this.mType = i;
            this.mUserId = i2;
            this.mSensor = i3;
        }

        TypeUserSensor(TypeUserSensor typeUserSensor) {
            this(typeUserSensor.mType, typeUserSensor.mUserId, typeUserSensor.mSensor);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TypeUserSensor)) {
                return false;
            }
            TypeUserSensor typeUserSensor = (TypeUserSensor) obj;
            return this.mType == typeUserSensor.mType && this.mUserId == typeUserSensor.mUserId && this.mSensor == typeUserSensor.mSensor;
        }

        public int hashCode() {
            return (((this.mType * 31) + this.mUserId) * 31) + this.mSensor;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void schedulePersist() {
        int size = this.mStates.size();
        ArrayMap arrayMap = new ArrayMap();
        for (int i = 0; i < size; i++) {
            arrayMap.put(new TypeUserSensor(this.mStates.keyAt(i)), new SensorState(this.mStates.valueAt(i)));
        }
        IoThread.getHandler().sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.sensorprivacy.PersistedState$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((PersistedState) obj).persist((ArrayMap) obj2);
            }
        }, this, arrayMap));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void persist(ArrayMap<TypeUserSensor, SensorState> arrayMap) {
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream startWrite = this.mAtomicFile.startWrite();
            try {
                TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
                resolveSerializer.startDocument((String) null, Boolean.TRUE);
                resolveSerializer.startTag((String) null, XML_TAG_SENSOR_PRIVACY);
                resolveSerializer.attributeInt((String) null, XML_ATTRIBUTE_PERSISTENCE_VERSION, 2);
                resolveSerializer.attributeInt((String) null, XML_ATTRIBUTE_VERSION, 2);
                for (int i = 0; i < arrayMap.size(); i++) {
                    TypeUserSensor keyAt = arrayMap.keyAt(i);
                    SensorState valueAt = arrayMap.valueAt(i);
                    if (keyAt.mType == 1) {
                        resolveSerializer.startTag((String) null, XML_TAG_SENSOR_STATE);
                        resolveSerializer.attributeInt((String) null, XML_ATTRIBUTE_TOGGLE_TYPE, keyAt.mType);
                        resolveSerializer.attributeInt((String) null, XML_ATTRIBUTE_USER_ID, keyAt.mUserId);
                        resolveSerializer.attributeInt((String) null, XML_ATTRIBUTE_SENSOR, keyAt.mSensor);
                        resolveSerializer.attributeInt((String) null, XML_ATTRIBUTE_STATE_TYPE, valueAt.getState());
                        resolveSerializer.attributeLong((String) null, XML_ATTRIBUTE_LAST_CHANGE, valueAt.getLastChange());
                        resolveSerializer.endTag((String) null, XML_TAG_SENSOR_STATE);
                    }
                }
                resolveSerializer.endTag((String) null, XML_TAG_SENSOR_PRIVACY);
                resolveSerializer.endDocument();
                this.mAtomicFile.finishWrite(startWrite);
            } catch (IOException e) {
                e = e;
                fileOutputStream = startWrite;
                Log.e(LOG_TAG, "Caught an exception persisting the sensor privacy state: ", e);
                this.mAtomicFile.failWrite(fileOutputStream);
            }
        } catch (IOException e2) {
            e = e2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(DualDumpOutputStream dualDumpOutputStream) {
        SparseArray sparseArray = new SparseArray();
        int size = this.mStates.size();
        for (int i = 0; i < size; i++) {
            int i2 = this.mStates.keyAt(i).mType;
            int i3 = this.mStates.keyAt(i).mUserId;
            int i4 = this.mStates.keyAt(i).mSensor;
            SparseArray sparseArray2 = (SparseArray) sparseArray.get(i3);
            if (sparseArray2 == null) {
                sparseArray2 = new SparseArray();
                sparseArray.put(i3, sparseArray2);
            }
            sparseArray2.put(i4, new Pair(Integer.valueOf(i2), this.mStates.valueAt(i)));
        }
        dualDumpOutputStream.write("storage_implementation", 1138166333444L, SensorPrivacyStateControllerImpl.class.getName());
        int size2 = sparseArray.size();
        for (int i5 = 0; i5 < size2; i5++) {
            int keyAt = sparseArray.keyAt(i5);
            long start = dualDumpOutputStream.start(DatabaseHelper.SoundModelContract.KEY_USERS, 2246267895811L);
            long j = 1120986464257L;
            dualDumpOutputStream.write("user_id", 1120986464257L, keyAt);
            SparseArray sparseArray3 = (SparseArray) sparseArray.valueAt(i5);
            int i6 = 0;
            for (int size3 = sparseArray3.size(); i6 < size3; size3 = size3) {
                int keyAt2 = sparseArray3.keyAt(i6);
                int intValue = ((Integer) ((Pair) sparseArray3.valueAt(i6)).first).intValue();
                SensorState sensorState = (SensorState) ((Pair) sparseArray3.valueAt(i6)).second;
                long start2 = dualDumpOutputStream.start("sensors", 2246267895812L);
                dualDumpOutputStream.write(XML_ATTRIBUTE_SENSOR, j, keyAt2);
                long start3 = dualDumpOutputStream.start("toggles", 2246267895810L);
                dualDumpOutputStream.write("toggle_type", 1159641169924L, intValue);
                dualDumpOutputStream.write("state_type", 1159641169925L, sensorState.getState());
                dualDumpOutputStream.write("last_change", 1112396529667L, sensorState.getLastChange());
                dualDumpOutputStream.end(start3);
                dualDumpOutputStream.end(start2);
                i6++;
                j = 1120986464257L;
                size2 = size2;
            }
            dualDumpOutputStream.end(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forEachKnownState(QuadConsumer<Integer, Integer, Integer, SensorState> quadConsumer) {
        int size = this.mStates.size();
        for (int i = 0; i < size; i++) {
            TypeUserSensor keyAt = this.mStates.keyAt(i);
            quadConsumer.accept(Integer.valueOf(keyAt.mType), Integer.valueOf(keyAt.mUserId), Integer.valueOf(keyAt.mSensor), this.mStates.valueAt(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PVersion0 {
        private SparseArray<SensorState> mIndividualEnabled;

        /* JADX INFO: Access modifiers changed from: private */
        public void upgrade() {
        }

        private PVersion0(int i) {
            this.mIndividualEnabled = new SparseArray<>();
            if (i != 0) {
                throw new RuntimeException("Only version 0 supported");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addState(int i, boolean z) {
            this.mIndividualEnabled.put(i, new SensorState(z));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PVersion1 {
        private SparseArray<SparseArray<SensorState>> mIndividualEnabled;

        /* JADX INFO: Access modifiers changed from: private */
        public void upgrade() {
        }

        private PVersion1(int i) {
            this.mIndividualEnabled = new SparseArray<>();
            if (i != 1) {
                throw new RuntimeException("Only version 1 supported");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static PVersion1 fromPVersion0(PVersion0 pVersion0) {
            pVersion0.upgrade();
            PVersion1 pVersion1 = new PVersion1(1);
            int[] iArr = {0};
            try {
                iArr = ((UserManagerInternal) LocalServices.getService(UserManagerInternal.class)).getUserIds();
            } catch (Exception e) {
                Log.e(PersistedState.LOG_TAG, "Unable to get users.", e);
            }
            for (int i : iArr) {
                for (int i2 = 0; i2 < pVersion0.mIndividualEnabled.size(); i2++) {
                    pVersion1.addState(i, pVersion0.mIndividualEnabled.keyAt(i2), ((SensorState) pVersion0.mIndividualEnabled.valueAt(i2)).isEnabled());
                }
            }
            return pVersion1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addState(int i, int i2, boolean z) {
            SparseArray<SensorState> sparseArray = this.mIndividualEnabled.get(i, new SparseArray<>());
            this.mIndividualEnabled.put(i, sparseArray);
            sparseArray.put(i2, new SensorState(z));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PVersion2 {
        private ArrayMap<TypeUserSensor, SensorState> mStates;

        private PVersion2(int i) {
            this.mStates = new ArrayMap<>();
            if (i != 2) {
                throw new RuntimeException("Only version 2 supported");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static PVersion2 fromPVersion1(PVersion1 pVersion1) {
            pVersion1.upgrade();
            PVersion2 pVersion2 = new PVersion2(2);
            SparseArray sparseArray = pVersion1.mIndividualEnabled;
            int size = sparseArray.size();
            for (int i = 0; i < size; i++) {
                int keyAt = sparseArray.keyAt(i);
                SparseArray sparseArray2 = (SparseArray) sparseArray.valueAt(i);
                int size2 = sparseArray2.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    int keyAt2 = sparseArray2.keyAt(i2);
                    SensorState sensorState = (SensorState) sparseArray2.valueAt(i2);
                    pVersion2.addState(1, keyAt, keyAt2, sensorState.getState(), sensorState.getLastChange());
                }
            }
            return pVersion2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addState(int i, int i2, int i3, int i4, long j) {
            this.mStates.put(new TypeUserSensor(i, i2, i3), new SensorState(i4, j));
        }
    }

    public void resetForTesting() {
        this.mStates = new ArrayMap<>();
    }
}
