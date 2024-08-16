package com.android.server.devicepolicy;

import android.app.admin.DevicePolicyDrawableResource;
import android.app.admin.DevicePolicyStringResource;
import android.app.admin.ParcelableResource;
import android.os.Environment;
import android.util.AtomicFile;
import android.util.Log;
import android.util.Xml;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DeviceManagementResourcesProvider {
    private static final String ATTR_DRAWABLE_ID = "drawable-id";
    private static final String ATTR_DRAWABLE_SOURCE = "drawable-source";
    private static final String ATTR_DRAWABLE_STYLE = "drawable-style";
    private static final String ATTR_SOURCE_ID = "source-id";
    private static final String TAG = "DevicePolicyManagerService";
    private static final String TAG_DRAWABLE_SOURCE_ENTRY = "drawable-source-entry";
    private static final String TAG_DRAWABLE_STYLE_ENTRY = "drawable-style-entry";
    private static final String TAG_ROOT = "root";
    private static final String TAG_STRING_ENTRY = "string-entry";
    private static final String UPDATED_RESOURCES_XML = "updated_resources.xml";
    private final Injector mInjector;
    private final Object mLock;
    private final Map<String, Map<String, Map<String, ParcelableResource>>> mUpdatedDrawablesForSource;
    private final Map<String, Map<String, ParcelableResource>> mUpdatedDrawablesForStyle;
    private final Map<String, ParcelableResource> mUpdatedStrings;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeviceManagementResourcesProvider() {
        this(new Injector());
    }

    DeviceManagementResourcesProvider(Injector injector) {
        this.mUpdatedDrawablesForStyle = new HashMap();
        this.mUpdatedDrawablesForSource = new HashMap();
        this.mUpdatedStrings = new HashMap();
        this.mLock = new Object();
        Objects.requireNonNull(injector);
        this.mInjector = injector;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateDrawables(List<DevicePolicyDrawableResource> list) {
        boolean updateDrawableForSource;
        boolean z = false;
        for (int i = 0; i < list.size(); i++) {
            String drawableId = list.get(i).getDrawableId();
            String drawableStyle = list.get(i).getDrawableStyle();
            String drawableSource = list.get(i).getDrawableSource();
            ParcelableResource resource = list.get(i).getResource();
            Objects.requireNonNull(drawableId, "drawableId must be provided.");
            Objects.requireNonNull(drawableStyle, "drawableStyle must be provided.");
            Objects.requireNonNull(drawableSource, "drawableSource must be provided.");
            Objects.requireNonNull(resource, "ParcelableResource must be provided.");
            if ("UNDEFINED".equals(drawableSource)) {
                updateDrawableForSource = updateDrawable(drawableId, drawableStyle, resource);
            } else {
                updateDrawableForSource = updateDrawableForSource(drawableId, drawableSource, drawableStyle, resource);
            }
            z |= updateDrawableForSource;
        }
        if (!z) {
            return false;
        }
        synchronized (this.mLock) {
            write();
        }
        return true;
    }

    private boolean updateDrawable(String str, String str2, ParcelableResource parcelableResource) {
        synchronized (this.mLock) {
            if (!this.mUpdatedDrawablesForStyle.containsKey(str)) {
                this.mUpdatedDrawablesForStyle.put(str, new HashMap());
            }
            if (parcelableResource.equals(this.mUpdatedDrawablesForStyle.get(str).get(str2))) {
                return false;
            }
            this.mUpdatedDrawablesForStyle.get(str).put(str2, parcelableResource);
            return true;
        }
    }

    private boolean updateDrawableForSource(String str, String str2, String str3, ParcelableResource parcelableResource) {
        synchronized (this.mLock) {
            if (!this.mUpdatedDrawablesForSource.containsKey(str)) {
                this.mUpdatedDrawablesForSource.put(str, new HashMap());
            }
            Map<String, Map<String, ParcelableResource>> map = this.mUpdatedDrawablesForSource.get(str);
            if (!map.containsKey(str2)) {
                this.mUpdatedDrawablesForSource.get(str).put(str2, new HashMap());
            }
            if (parcelableResource.equals(map.get(str2).get(str3))) {
                return false;
            }
            map.get(str2).put(str3, parcelableResource);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removeDrawables(List<String> list) {
        synchronized (this.mLock) {
            int i = 0;
            boolean z = false;
            while (true) {
                boolean z2 = true;
                if (i >= list.size()) {
                    break;
                }
                String str = list.get(i);
                if (this.mUpdatedDrawablesForStyle.remove(str) == null && this.mUpdatedDrawablesForSource.remove(str) == null) {
                    z2 = false;
                }
                z |= z2;
                i++;
            }
            if (!z) {
                return false;
            }
            write();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ParcelableResource getDrawable(String str, String str2, String str3) {
        synchronized (this.mLock) {
            ParcelableResource drawableForSourceLocked = getDrawableForSourceLocked(str, str2, str3);
            if (drawableForSourceLocked != null) {
                return drawableForSourceLocked;
            }
            if (!this.mUpdatedDrawablesForStyle.containsKey(str)) {
                return null;
            }
            return this.mUpdatedDrawablesForStyle.get(str).get(str2);
        }
    }

    ParcelableResource getDrawableForSourceLocked(String str, String str2, String str3) {
        if (this.mUpdatedDrawablesForSource.containsKey(str) && this.mUpdatedDrawablesForSource.get(str).containsKey(str3)) {
            return this.mUpdatedDrawablesForSource.get(str).get(str3).get(str2);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateStrings(List<DevicePolicyStringResource> list) {
        boolean z = false;
        for (int i = 0; i < list.size(); i++) {
            String stringId = list.get(i).getStringId();
            ParcelableResource resource = list.get(i).getResource();
            Objects.requireNonNull(stringId, "stringId must be provided.");
            Objects.requireNonNull(resource, "ParcelableResource must be provided.");
            z |= updateString(stringId, resource);
        }
        if (!z) {
            return false;
        }
        synchronized (this.mLock) {
            write();
        }
        return true;
    }

    private boolean updateString(String str, ParcelableResource parcelableResource) {
        synchronized (this.mLock) {
            if (parcelableResource.equals(this.mUpdatedStrings.get(str))) {
                return false;
            }
            this.mUpdatedStrings.put(str, parcelableResource);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removeStrings(List<String> list) {
        synchronized (this.mLock) {
            int i = 0;
            boolean z = false;
            while (true) {
                boolean z2 = true;
                if (i >= list.size()) {
                    break;
                }
                if (this.mUpdatedStrings.remove(list.get(i)) == null) {
                    z2 = false;
                }
                z |= z2;
                i++;
            }
            if (!z) {
                return false;
            }
            write();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ParcelableResource getString(String str) {
        ParcelableResource parcelableResource;
        synchronized (this.mLock) {
            parcelableResource = this.mUpdatedStrings.get(str);
        }
        return parcelableResource;
    }

    private void write() {
        Log.d(TAG, "Writing updated resources to file.");
        new ResourcesReaderWriter().writeToFileLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void load() {
        synchronized (this.mLock) {
            new ResourcesReaderWriter().readFromFileLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public File getResourcesFile() {
        return new File(this.mInjector.environmentGetDataSystemDirectory(), UPDATED_RESOURCES_XML);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class ResourcesReaderWriter {
        private final File mFile;

        private ResourcesReaderWriter() {
            this.mFile = DeviceManagementResourcesProvider.this.getResourcesFile();
        }

        void writeToFileLocked() {
            Log.d(DeviceManagementResourcesProvider.TAG, "Writing to " + this.mFile);
            AtomicFile atomicFile = new AtomicFile(this.mFile);
            FileOutputStream fileOutputStream = null;
            try {
                FileOutputStream startWrite = atomicFile.startWrite();
                try {
                    TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
                    resolveSerializer.startDocument((String) null, Boolean.TRUE);
                    resolveSerializer.startTag((String) null, DeviceManagementResourcesProvider.TAG_ROOT);
                    writeInner(resolveSerializer);
                    resolveSerializer.endTag((String) null, DeviceManagementResourcesProvider.TAG_ROOT);
                    resolveSerializer.endDocument();
                    resolveSerializer.flush();
                    atomicFile.finishWrite(startWrite);
                } catch (IOException e) {
                    e = e;
                    fileOutputStream = startWrite;
                    Log.e(DeviceManagementResourcesProvider.TAG, "Exception when writing", e);
                    if (fileOutputStream != null) {
                        atomicFile.failWrite(fileOutputStream);
                    }
                }
            } catch (IOException e2) {
                e = e2;
            }
        }

        void readFromFileLocked() {
            if (!this.mFile.exists()) {
                Log.d(DeviceManagementResourcesProvider.TAG, "" + this.mFile + " doesn't exist");
                return;
            }
            Log.d(DeviceManagementResourcesProvider.TAG, "Reading from " + this.mFile);
            FileInputStream fileInputStream = null;
            try {
                try {
                    fileInputStream = new AtomicFile(this.mFile).openRead();
                    TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(fileInputStream);
                    int i = 0;
                    while (true) {
                        int next = resolvePullParser.next();
                        if (next == 1) {
                            break;
                        }
                        if (next == 2) {
                            i++;
                            String name = resolvePullParser.getName();
                            if (i == 1) {
                                if (!DeviceManagementResourcesProvider.TAG_ROOT.equals(name)) {
                                    Log.e(DeviceManagementResourcesProvider.TAG, "Invalid root tag: " + name);
                                    return;
                                }
                            } else if (!readInner(resolvePullParser, i, name)) {
                                return;
                            }
                        } else if (next == 3) {
                            i--;
                        }
                    }
                } catch (IOException | XmlPullParserException e) {
                    Log.e(DeviceManagementResourcesProvider.TAG, "Error parsing resources file", e);
                }
            } finally {
                IoUtils.closeQuietly(fileInputStream);
            }
        }

        void writeInner(TypedXmlSerializer typedXmlSerializer) throws IOException {
            writeDrawablesForStylesInner(typedXmlSerializer);
            writeDrawablesForSourcesInner(typedXmlSerializer);
            writeStringsInner(typedXmlSerializer);
        }

        private void writeDrawablesForStylesInner(TypedXmlSerializer typedXmlSerializer) throws IOException {
            if (DeviceManagementResourcesProvider.this.mUpdatedDrawablesForStyle == null || DeviceManagementResourcesProvider.this.mUpdatedDrawablesForStyle.isEmpty()) {
                return;
            }
            for (Map.Entry entry : DeviceManagementResourcesProvider.this.mUpdatedDrawablesForStyle.entrySet()) {
                for (Map.Entry entry2 : ((Map) entry.getValue()).entrySet()) {
                    typedXmlSerializer.startTag((String) null, DeviceManagementResourcesProvider.TAG_DRAWABLE_STYLE_ENTRY);
                    typedXmlSerializer.attribute((String) null, DeviceManagementResourcesProvider.ATTR_DRAWABLE_ID, (String) entry.getKey());
                    typedXmlSerializer.attribute((String) null, DeviceManagementResourcesProvider.ATTR_DRAWABLE_STYLE, (String) entry2.getKey());
                    ((ParcelableResource) entry2.getValue()).writeToXmlFile(typedXmlSerializer);
                    typedXmlSerializer.endTag((String) null, DeviceManagementResourcesProvider.TAG_DRAWABLE_STYLE_ENTRY);
                }
            }
        }

        private void writeDrawablesForSourcesInner(TypedXmlSerializer typedXmlSerializer) throws IOException {
            if (DeviceManagementResourcesProvider.this.mUpdatedDrawablesForSource == null || DeviceManagementResourcesProvider.this.mUpdatedDrawablesForSource.isEmpty()) {
                return;
            }
            for (Map.Entry entry : DeviceManagementResourcesProvider.this.mUpdatedDrawablesForSource.entrySet()) {
                for (Map.Entry entry2 : ((Map) entry.getValue()).entrySet()) {
                    for (Map.Entry entry3 : ((Map) entry2.getValue()).entrySet()) {
                        typedXmlSerializer.startTag((String) null, DeviceManagementResourcesProvider.TAG_DRAWABLE_SOURCE_ENTRY);
                        typedXmlSerializer.attribute((String) null, DeviceManagementResourcesProvider.ATTR_DRAWABLE_ID, (String) entry.getKey());
                        typedXmlSerializer.attribute((String) null, DeviceManagementResourcesProvider.ATTR_DRAWABLE_SOURCE, (String) entry2.getKey());
                        typedXmlSerializer.attribute((String) null, DeviceManagementResourcesProvider.ATTR_DRAWABLE_STYLE, (String) entry3.getKey());
                        ((ParcelableResource) entry3.getValue()).writeToXmlFile(typedXmlSerializer);
                        typedXmlSerializer.endTag((String) null, DeviceManagementResourcesProvider.TAG_DRAWABLE_SOURCE_ENTRY);
                    }
                }
            }
        }

        private void writeStringsInner(TypedXmlSerializer typedXmlSerializer) throws IOException {
            if (DeviceManagementResourcesProvider.this.mUpdatedStrings == null || DeviceManagementResourcesProvider.this.mUpdatedStrings.isEmpty()) {
                return;
            }
            for (Map.Entry entry : DeviceManagementResourcesProvider.this.mUpdatedStrings.entrySet()) {
                typedXmlSerializer.startTag((String) null, DeviceManagementResourcesProvider.TAG_STRING_ENTRY);
                typedXmlSerializer.attribute((String) null, DeviceManagementResourcesProvider.ATTR_SOURCE_ID, (String) entry.getKey());
                ((ParcelableResource) entry.getValue()).writeToXmlFile(typedXmlSerializer);
                typedXmlSerializer.endTag((String) null, DeviceManagementResourcesProvider.TAG_STRING_ENTRY);
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x0019, code lost:
        
            if (r8.equals(com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG_DRAWABLE_STYLE_ENTRY) == false) goto L7;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private boolean readInner(TypedXmlPullParser typedXmlPullParser, int i, String str) throws XmlPullParserException, IOException {
            char c = 2;
            if (i > 2) {
                return true;
            }
            str.hashCode();
            switch (str.hashCode()) {
                case -1021023306:
                    if (str.equals(DeviceManagementResourcesProvider.TAG_STRING_ENTRY)) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 1224071439:
                    if (str.equals(DeviceManagementResourcesProvider.TAG_DRAWABLE_SOURCE_ENTRY)) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 1406273191:
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    DeviceManagementResourcesProvider.this.mUpdatedStrings.put(typedXmlPullParser.getAttributeValue((String) null, DeviceManagementResourcesProvider.ATTR_SOURCE_ID), ParcelableResource.createFromXml(typedXmlPullParser));
                    return true;
                case 1:
                    String attributeValue = typedXmlPullParser.getAttributeValue((String) null, DeviceManagementResourcesProvider.ATTR_DRAWABLE_ID);
                    String attributeValue2 = typedXmlPullParser.getAttributeValue((String) null, DeviceManagementResourcesProvider.ATTR_DRAWABLE_SOURCE);
                    String attributeValue3 = typedXmlPullParser.getAttributeValue((String) null, DeviceManagementResourcesProvider.ATTR_DRAWABLE_STYLE);
                    ParcelableResource createFromXml = ParcelableResource.createFromXml(typedXmlPullParser);
                    if (!DeviceManagementResourcesProvider.this.mUpdatedDrawablesForSource.containsKey(attributeValue)) {
                        DeviceManagementResourcesProvider.this.mUpdatedDrawablesForSource.put(attributeValue, new HashMap());
                    }
                    if (!((Map) DeviceManagementResourcesProvider.this.mUpdatedDrawablesForSource.get(attributeValue)).containsKey(attributeValue2)) {
                        ((Map) DeviceManagementResourcesProvider.this.mUpdatedDrawablesForSource.get(attributeValue)).put(attributeValue2, new HashMap());
                    }
                    ((Map) ((Map) DeviceManagementResourcesProvider.this.mUpdatedDrawablesForSource.get(attributeValue)).get(attributeValue2)).put(attributeValue3, createFromXml);
                    return true;
                case 2:
                    String attributeValue4 = typedXmlPullParser.getAttributeValue((String) null, DeviceManagementResourcesProvider.ATTR_DRAWABLE_ID);
                    String attributeValue5 = typedXmlPullParser.getAttributeValue((String) null, DeviceManagementResourcesProvider.ATTR_DRAWABLE_STYLE);
                    ParcelableResource createFromXml2 = ParcelableResource.createFromXml(typedXmlPullParser);
                    if (!DeviceManagementResourcesProvider.this.mUpdatedDrawablesForStyle.containsKey(attributeValue4)) {
                        DeviceManagementResourcesProvider.this.mUpdatedDrawablesForStyle.put(attributeValue4, new HashMap());
                    }
                    ((Map) DeviceManagementResourcesProvider.this.mUpdatedDrawablesForStyle.get(attributeValue4)).put(attributeValue5, createFromXml2);
                    return true;
                default:
                    Log.e(DeviceManagementResourcesProvider.TAG, "Unexpected tag: " + str);
                    return false;
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector {
        File environmentGetDataSystemDirectory() {
            return Environment.getDataSystemDirectory();
        }
    }
}
