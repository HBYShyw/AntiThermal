package com.android.server.devicepolicy;

import android.content.ComponentName;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class TransferOwnershipMetadataManager {
    static final String ADMIN_TYPE_DEVICE_OWNER = "device-owner";
    static final String ADMIN_TYPE_PROFILE_OWNER = "profile-owner";
    public static final String OWNER_TRANSFER_METADATA_XML = "owner-transfer-metadata.xml";
    private static final String TAG = "com.android.server.devicepolicy.TransferOwnershipMetadataManager";

    @VisibleForTesting
    static final String TAG_ADMIN_TYPE = "admin-type";

    @VisibleForTesting
    static final String TAG_SOURCE_COMPONENT = "source-component";

    @VisibleForTesting
    static final String TAG_TARGET_COMPONENT = "target-component";

    @VisibleForTesting
    static final String TAG_USER_ID = "user-id";
    private final Injector mInjector;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TransferOwnershipMetadataManager() {
        this(new Injector());
    }

    @VisibleForTesting
    TransferOwnershipMetadataManager(Injector injector) {
        this.mInjector = injector;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean saveMetadataFile(Metadata metadata) {
        File file = new File(this.mInjector.getOwnerTransferMetadataDir(), OWNER_TRANSFER_METADATA_XML);
        AtomicFile atomicFile = new AtomicFile(file);
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream startWrite = atomicFile.startWrite();
            try {
                TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
                resolveSerializer.startDocument((String) null, Boolean.TRUE);
                insertSimpleTag(resolveSerializer, TAG_USER_ID, Integer.toString(metadata.userId));
                insertSimpleTag(resolveSerializer, TAG_SOURCE_COMPONENT, metadata.sourceComponent.flattenToString());
                insertSimpleTag(resolveSerializer, TAG_TARGET_COMPONENT, metadata.targetComponent.flattenToString());
                insertSimpleTag(resolveSerializer, TAG_ADMIN_TYPE, metadata.adminType);
                resolveSerializer.endDocument();
                atomicFile.finishWrite(startWrite);
                return true;
            } catch (IOException e) {
                e = e;
                fileOutputStream = startWrite;
                Slog.e(TAG, "Caught exception while trying to save Owner Transfer Params to file " + file, e);
                file.delete();
                atomicFile.failWrite(fileOutputStream);
                return false;
            }
        } catch (IOException e2) {
            e = e2;
        }
    }

    private void insertSimpleTag(TypedXmlSerializer typedXmlSerializer, String str, String str2) throws IOException {
        typedXmlSerializer.startTag((String) null, str);
        typedXmlSerializer.text(str2);
        typedXmlSerializer.endTag((String) null, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Metadata loadMetadataFile() {
        File file = new File(this.mInjector.getOwnerTransferMetadataDir(), OWNER_TRANSFER_METADATA_XML);
        if (!file.exists()) {
            return null;
        }
        Slog.d(TAG, "Loading TransferOwnershipMetadataManager from " + file);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                Metadata parseMetadataFile = parseMetadataFile(Xml.resolvePullParser(fileInputStream));
                fileInputStream.close();
                return parseMetadataFile;
            } catch (Throwable th) {
                try {
                    fileInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException | IllegalArgumentException | XmlPullParserException e) {
            Slog.e(TAG, "Caught exception while trying to load the owner transfer params from file " + file, e);
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x004e, code lost:
    
        if (r5.equals(com.android.server.devicepolicy.TransferOwnershipMetadataManager.TAG_USER_ID) == false) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private Metadata parseMetadataFile(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        int depth = typedXmlPullParser.getDepth();
        String str = null;
        int i = 0;
        String str2 = null;
        String str3 = null;
        while (true) {
            int next = typedXmlPullParser.next();
            char c = 1;
            if (next != 1 && (next != 3 || typedXmlPullParser.getDepth() > depth)) {
                if (next != 3 && next != 4) {
                    String name = typedXmlPullParser.getName();
                    name.hashCode();
                    switch (name.hashCode()) {
                        case -337219647:
                            if (name.equals(TAG_TARGET_COMPONENT)) {
                                c = 0;
                                break;
                            }
                            break;
                        case -147180963:
                            break;
                        case 281362891:
                            if (name.equals(TAG_SOURCE_COMPONENT)) {
                                c = 2;
                                break;
                            }
                            break;
                        case 641951480:
                            if (name.equals(TAG_ADMIN_TYPE)) {
                                c = 3;
                                break;
                            }
                            break;
                    }
                    c = 65535;
                    switch (c) {
                        case 0:
                            typedXmlPullParser.next();
                            str2 = typedXmlPullParser.getText();
                            break;
                        case 1:
                            typedXmlPullParser.next();
                            i = Integer.parseInt(typedXmlPullParser.getText());
                            break;
                        case 2:
                            typedXmlPullParser.next();
                            str = typedXmlPullParser.getText();
                            break;
                        case 3:
                            typedXmlPullParser.next();
                            str3 = typedXmlPullParser.getText();
                            break;
                    }
                }
            }
        }
        return new Metadata(str, str2, i, str3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deleteMetadataFile() {
        new File(this.mInjector.getOwnerTransferMetadataDir(), OWNER_TRANSFER_METADATA_XML).delete();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean metadataFileExists() {
        return new File(this.mInjector.getOwnerTransferMetadataDir(), OWNER_TRANSFER_METADATA_XML).exists();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Metadata {
        final String adminType;
        final ComponentName sourceComponent;
        final ComponentName targetComponent;
        final int userId;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Metadata(ComponentName componentName, ComponentName componentName2, int i, String str) {
            this.sourceComponent = componentName;
            this.targetComponent = componentName2;
            Objects.requireNonNull(componentName);
            Objects.requireNonNull(componentName2);
            Preconditions.checkStringNotEmpty(str);
            this.userId = i;
            this.adminType = str;
        }

        Metadata(String str, String str2, int i, String str3) {
            this(unflattenComponentUnchecked(str), unflattenComponentUnchecked(str2), i, str3);
        }

        private static ComponentName unflattenComponentUnchecked(String str) {
            Objects.requireNonNull(str);
            return ComponentName.unflattenFromString(str);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Metadata)) {
                return false;
            }
            Metadata metadata = (Metadata) obj;
            return this.userId == metadata.userId && this.sourceComponent.equals(metadata.sourceComponent) && this.targetComponent.equals(metadata.targetComponent) && TextUtils.equals(this.adminType, metadata.adminType);
        }

        public int hashCode() {
            return ((((((this.userId + 31) * 31) + this.sourceComponent.hashCode()) * 31) + this.targetComponent.hashCode()) * 31) + this.adminType.hashCode();
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static class Injector {
        Injector() {
        }

        public File getOwnerTransferMetadataDir() {
            return Environment.getDataSystemDirectory();
        }
    }
}
