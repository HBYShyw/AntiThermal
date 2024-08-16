package com.android.server.power.stats.wakeups;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.LongSparseArray;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class IrqDeviceMap {
    private static final String ATTR_NAME = "name";
    private static final String TAG_DEVICE = "device";
    private static final String TAG_IRQ_DEVICE_MAP = "irq-device-map";
    private static final String TAG_SUBSYSTEM = "subsystem";
    private static LongSparseArray<IrqDeviceMap> sInstanceMap = new LongSparseArray<>(1);
    private final ArrayMap<String, List<String>> mSubsystemsForDevice = new ArrayMap<>();

    private IrqDeviceMap(XmlResourceParser xmlResourceParser) {
        try {
            try {
                try {
                    XmlUtils.beginDocument(xmlResourceParser, TAG_IRQ_DEVICE_MAP);
                    ArraySet arraySet = new ArraySet();
                    String str = null;
                    while (true) {
                        int eventType = xmlResourceParser.getEventType();
                        if (eventType == 1) {
                            return;
                        }
                        if (eventType == 2 && xmlResourceParser.getName().equals(TAG_DEVICE)) {
                            str = xmlResourceParser.getAttributeValue(null, "name");
                        }
                        if (str != null && eventType == 3 && xmlResourceParser.getName().equals(TAG_DEVICE)) {
                            if (arraySet.size() > 0) {
                                this.mSubsystemsForDevice.put(str, Collections.unmodifiableList(new ArrayList(arraySet)));
                            }
                            arraySet.clear();
                            str = null;
                        }
                        if (str != null && eventType == 2 && xmlResourceParser.getName().equals(TAG_SUBSYSTEM)) {
                            xmlResourceParser.next();
                            if (xmlResourceParser.getEventType() == 4) {
                                arraySet.add(xmlResourceParser.getText());
                            }
                        }
                        xmlResourceParser.next();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (XmlPullParserException e2) {
                throw new RuntimeException(e2);
            }
        } finally {
            xmlResourceParser.close();
        }
    }

    public static IrqDeviceMap getInstance(Context context, int i) {
        synchronized (IrqDeviceMap.class) {
            long j = i;
            int indexOfKey = sInstanceMap.indexOfKey(j);
            if (indexOfKey >= 0) {
                return sInstanceMap.valueAt(indexOfKey);
            }
            IrqDeviceMap irqDeviceMap = new IrqDeviceMap(context.getResources().getXml(i));
            synchronized (IrqDeviceMap.class) {
                sInstanceMap.put(j, irqDeviceMap);
            }
            return irqDeviceMap;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<String> getSubsystemsForDevice(String str) {
        return this.mSubsystemsForDevice.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        LongSparseArray<IrqDeviceMap> longSparseArray;
        indentingPrintWriter.println("Irq device map:");
        indentingPrintWriter.increaseIndent();
        synchronized (IrqDeviceMap.class) {
            longSparseArray = sInstanceMap;
        }
        int indexOfValue = longSparseArray.indexOfValue(this);
        indentingPrintWriter.println("Loaded from xml resource: " + (indexOfValue >= 0 ? "0x" + Long.toHexString(longSparseArray.keyAt(indexOfValue)) : null));
        indentingPrintWriter.println("Map:");
        indentingPrintWriter.increaseIndent();
        for (int i = 0; i < this.mSubsystemsForDevice.size(); i++) {
            indentingPrintWriter.print(this.mSubsystemsForDevice.keyAt(i) + ": ");
            indentingPrintWriter.println(this.mSubsystemsForDevice.valueAt(i));
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
    }
}
