package com.android.server.display;

import android.os.Environment;
import android.util.IndentingPrintWriter;
import android.util.Slog;
import android.util.SparseArray;
import android.view.DisplayAddress;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.display.config.layout.Display;
import com.android.server.display.config.layout.Layouts;
import com.android.server.display.config.layout.XmlParser;
import com.android.server.display.layout.DisplayIdProducer;
import com.android.server.display.layout.Layout;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DeviceStateToLayoutMap {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String CONFIG_FILE_PATH = "etc/displayconfig/display_layout_configuration.xml";
    private static final String CONFIG_FILE_PATH_PRIVATE = "etc/displayconfig/display_layout_configuration_private.xml";
    private static final String FRONT_STRING = "front";
    private static final int POSITION_FRONT = 0;
    private static final int POSITION_REAR = 1;
    private static final int POSITION_UNKNOWN = -1;
    private static final String REAR_STRING = "rear";
    public static final int STATE_DEFAULT = -1;
    private static final String TAG = "DeviceStateToLayoutMap";
    private IDeviceStateToLayoutMapExt mDeviceStateToLayoutMapExt;
    private final DisplayIdProducer mIdProducer;
    private final SparseArray<Layout> mLayoutMap;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeviceStateToLayoutMap(DisplayIdProducer displayIdProducer) {
        this(displayIdProducer, Environment.buildPath(Environment.getVendorDirectory(), new String[]{CONFIG_FILE_PATH}));
    }

    DeviceStateToLayoutMap(DisplayIdProducer displayIdProducer, File file) {
        this.mLayoutMap = new SparseArray<>();
        this.mDeviceStateToLayoutMapExt = (IDeviceStateToLayoutMapExt) ExtLoader.type(IDeviceStateToLayoutMapExt.class).base(this).create();
        this.mIdProducer = displayIdProducer;
        loadLayoutsFromConfig(file);
        createLayout(-1);
    }

    public SparseArray<Layout> getLayoutMap() {
        return this.mLayoutMap;
    }

    public void dumpLocked(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("DeviceStateToLayoutMap:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Registered Layouts:");
        for (int i = 0; i < this.mLayoutMap.size(); i++) {
            indentingPrintWriter.println("state(" + this.mLayoutMap.keyAt(i) + "): " + this.mLayoutMap.valueAt(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Layout get(int i) {
        Layout layout = this.mLayoutMap.get(i);
        return layout == null ? this.mLayoutMap.get(-1) : layout;
    }

    int size() {
        return this.mLayoutMap.size();
    }

    @VisibleForTesting
    void loadLayoutsFromConfig(File file) {
        File buildPath = Environment.buildPath(Environment.getOdmDirectory(), new String[]{CONFIG_FILE_PATH_PRIVATE});
        if (!buildPath.exists()) {
            buildPath = Environment.buildPath(Environment.getOdmDirectory(), new String[]{CONFIG_FILE_PATH});
        }
        if (!buildPath.exists()) {
            buildPath = Environment.buildPath(Environment.getVendorDirectory(), new String[]{CONFIG_FILE_PATH});
        }
        if (!buildPath.exists()) {
            return;
        }
        Slog.i(TAG, "Loading display layouts from " + buildPath);
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(buildPath));
            try {
                Layouts read = XmlParser.read(bufferedInputStream);
                if (read == null) {
                    Slog.i(TAG, "Display layout config not found: " + buildPath);
                    bufferedInputStream.close();
                    return;
                }
                for (com.android.server.display.config.layout.Layout layout : read.getLayout()) {
                    int intValue = layout.getState().intValue();
                    Layout createLayout = createLayout(intValue);
                    for (Display display : layout.getDisplay()) {
                        int position = getPosition(display.getPosition());
                        this.mDeviceStateToLayoutMapExt.updateRealDisplayAddressId(display, intValue);
                        int i = intValue;
                        createLayout.createDisplayLocked(DisplayAddress.fromPhysicalDisplayId(display.getAddress().longValue()), display.isDefaultDisplay(), display.isEnabled(), display.getDisplayGroup(), this.mIdProducer, position, 0, display.getBrightnessThrottlingMapId(), display.getRefreshRateZoneId(), display.getRefreshRateThermalThrottlingMapId());
                        intValue = i;
                    }
                }
                bufferedInputStream.close();
            } finally {
            }
        } catch (IOException | DatatypeConfigurationException | XmlPullParserException e) {
            Slog.e(TAG, "Encountered an error while reading/parsing display layout config file: " + buildPath, e);
        }
    }

    private int getPosition(String str) {
        if (FRONT_STRING.equals(str)) {
            return 0;
        }
        return REAR_STRING.equals(str) ? 1 : -1;
    }

    private Layout createLayout(int i) {
        if (this.mLayoutMap.contains(i)) {
            Slog.e(TAG, "Attempted to create a second layout for state " + i);
            return null;
        }
        Layout layout = new Layout();
        this.mLayoutMap.append(i, layout);
        return layout;
    }
}
