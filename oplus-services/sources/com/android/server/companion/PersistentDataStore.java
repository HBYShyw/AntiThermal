package com.android.server.companion;

import android.annotation.SuppressLint;
import android.companion.AssociationInfo;
import android.content.pm.UserInfo;
import android.net.MacAddress;
import android.os.Environment;
import android.util.ArrayMap;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"LongLogTag"})
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class PersistentDataStore {
    private static final int CURRENT_PERSISTENCE_VERSION = 1;
    private static final boolean DEBUG = false;
    private static final String FILE_NAME = "companion_device_manager.xml";
    private static final String FILE_NAME_LEGACY = "companion_device_manager_associations.xml";
    private static final String LEGACY_XML_ATTR_DEVICE = "device";
    private static final String TAG = "CompanionDevice_PersistentDataStore";
    private static final String XML_ATTR_DISPLAY_NAME = "display_name";
    private static final String XML_ATTR_ID = "id";
    private static final String XML_ATTR_LAST_TIME_CONNECTED = "last_time_connected";
    private static final String XML_ATTR_MAC_ADDRESS = "mac_address";
    private static final String XML_ATTR_NOTIFY_DEVICE_NEARBY = "notify_device_nearby";
    private static final String XML_ATTR_PACKAGE = "package";
    private static final String XML_ATTR_PACKAGE_NAME = "package_name";
    private static final String XML_ATTR_PERSISTENCE_VERSION = "persistence-version";
    private static final String XML_ATTR_PROFILE = "profile";
    private static final String XML_ATTR_REVOKED = "revoked";
    private static final String XML_ATTR_SELF_MANAGED = "self_managed";
    private static final String XML_ATTR_SYSTEM_DATA_SYNC_FLAGS = "system_data_sync_flags";
    private static final String XML_ATTR_TIME_APPROVED = "time_approved";
    private static final String XML_TAG_ASSOCIATION = "association";
    private static final String XML_TAG_ASSOCIATIONS = "associations";
    private static final String XML_TAG_ID = "id";
    private static final String XML_TAG_PACKAGE = "package";
    private static final String XML_TAG_PREVIOUSLY_USED_IDS = "previously-used-ids";
    private static final String XML_TAG_STATE = "state";
    private final ConcurrentMap<Integer, AtomicFile> mUserIdToStorageFile = new ConcurrentHashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void readStateForUsers(List<UserInfo> list, Set<AssociationInfo> set, SparseArray<Map<String, Set<Integer>>> sparseArray) {
        Iterator<UserInfo> it = list.iterator();
        while (it.hasNext()) {
            int i = it.next().id;
            Map<String, Set<Integer>> arrayMap = new ArrayMap<>();
            HashSet hashSet = new HashSet();
            readStateForUser(i, hashSet, arrayMap);
            int firstAssociationIdForUser = CompanionDeviceManagerService.getFirstAssociationIdForUser(i);
            int lastAssociationIdForUser = CompanionDeviceManagerService.getLastAssociationIdForUser(i);
            Iterator it2 = hashSet.iterator();
            while (it2.hasNext()) {
                int id = ((AssociationInfo) it2.next()).getId();
                if (id < firstAssociationIdForUser || id > lastAssociationIdForUser) {
                    Slog.e(TAG, "Wrong association ID assignment: " + id + ". Association belongs to u" + i + " and thus its ID should be within [" + firstAssociationIdForUser + ", " + lastAssociationIdForUser + "] range.");
                }
            }
            set.addAll(hashSet);
            sparseArray.append(i, arrayMap);
        }
    }

    void readStateForUser(int i, Collection<AssociationInfo> collection, Map<String, Set<Integer>> map) {
        String str;
        File file;
        AtomicFile atomicFile;
        Slog.i(TAG, "Reading associations for user " + i + " from disk");
        AtomicFile storageFileForUser = getStorageFileForUser(i);
        synchronized (storageFileForUser) {
            if (storageFileForUser.getBaseFile().exists()) {
                str = XML_TAG_STATE;
                file = null;
                atomicFile = storageFileForUser;
            } else {
                file = getBaseLegacyStorageFileForUser(i);
                if (!file.exists()) {
                    return;
                }
                AtomicFile atomicFile2 = new AtomicFile(file);
                str = XML_TAG_ASSOCIATIONS;
                atomicFile = atomicFile2;
            }
            int readStateFromFileLocked = readStateFromFileLocked(i, atomicFile, str, collection, map);
            if (file != null || readStateFromFileLocked < 1) {
                persistStateToFileLocked(storageFileForUser, collection, map);
                if (file != null) {
                    file.delete();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void persistStateForUser(int i, Collection<AssociationInfo> collection, Map<String, Set<Integer>> map) {
        Slog.i(TAG, "Writing associations for user " + i + " to disk");
        AtomicFile storageFileForUser = getStorageFileForUser(i);
        synchronized (storageFileForUser) {
            persistStateToFileLocked(storageFileForUser, collection, map);
        }
    }

    private int readStateFromFileLocked(int i, AtomicFile atomicFile, String str, Collection<AssociationInfo> collection, Map<String, Set<Integer>> map) {
        try {
            FileInputStream openRead = atomicFile.openRead();
            try {
                TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(openRead);
                XmlUtils.beginDocument(resolvePullParser, str);
                int readIntAttribute = XmlUtils.readIntAttribute(resolvePullParser, XML_ATTR_PERSISTENCE_VERSION, 0);
                if (readIntAttribute == 0) {
                    readAssociationsV0(resolvePullParser, i, collection);
                } else if (readIntAttribute == 1) {
                    while (true) {
                        resolvePullParser.nextTag();
                        if (DataStoreUtils.isStartOfTag(resolvePullParser, XML_TAG_ASSOCIATIONS)) {
                            readAssociationsV1(resolvePullParser, i, collection);
                        } else if (DataStoreUtils.isStartOfTag(resolvePullParser, XML_TAG_PREVIOUSLY_USED_IDS)) {
                            readPreviouslyUsedIdsV1(resolvePullParser, map);
                        } else if (DataStoreUtils.isEndOfTag(resolvePullParser, str)) {
                            break;
                        }
                    }
                }
                if (openRead != null) {
                    openRead.close();
                }
                return readIntAttribute;
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
        } catch (IOException | XmlPullParserException e) {
            Slog.e(TAG, "Error while reading associations file", e);
            return -1;
        }
    }

    private void persistStateToFileLocked(AtomicFile atomicFile, final Collection<AssociationInfo> collection, final Map<String, Set<Integer>> map) {
        DataStoreUtils.writeToFileSafely(atomicFile, new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.companion.PersistentDataStore$$ExternalSyntheticLambda2
            public final void acceptOrThrow(Object obj) {
                PersistentDataStore.lambda$persistStateToFileLocked$0(collection, map, (FileOutputStream) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$persistStateToFileLocked$0(Collection collection, Map map, FileOutputStream fileOutputStream) throws Exception {
        TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(fileOutputStream);
        resolveSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        resolveSerializer.startDocument((String) null, Boolean.TRUE);
        resolveSerializer.startTag((String) null, XML_TAG_STATE);
        XmlUtils.writeIntAttribute(resolveSerializer, XML_ATTR_PERSISTENCE_VERSION, 1);
        writeAssociations(resolveSerializer, collection);
        writePreviouslyUsedIds(resolveSerializer, map);
        resolveSerializer.endTag((String) null, XML_TAG_STATE);
        resolveSerializer.endDocument();
    }

    private AtomicFile getStorageFileForUser(final int i) {
        return this.mUserIdToStorageFile.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.server.companion.PersistentDataStore$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                AtomicFile createStorageFileForUser;
                createStorageFileForUser = DataStoreUtils.createStorageFileForUser(i, PersistentDataStore.FILE_NAME);
                return createStorageFileForUser;
            }
        });
    }

    private static File getBaseLegacyStorageFileForUser(int i) {
        return new File(Environment.getUserSystemDirectory(i), FILE_NAME_LEGACY);
    }

    private static void readAssociationsV0(TypedXmlPullParser typedXmlPullParser, int i, Collection<AssociationInfo> collection) throws XmlPullParserException, IOException {
        requireStartOfTag(typedXmlPullParser, XML_TAG_ASSOCIATIONS);
        int firstAssociationIdForUser = CompanionDeviceManagerService.getFirstAssociationIdForUser(i);
        while (true) {
            typedXmlPullParser.nextTag();
            if (DataStoreUtils.isEndOfTag(typedXmlPullParser, XML_TAG_ASSOCIATIONS)) {
                return;
            }
            if (DataStoreUtils.isStartOfTag(typedXmlPullParser, XML_TAG_ASSOCIATION)) {
                readAssociationV0(typedXmlPullParser, i, firstAssociationIdForUser, collection);
                firstAssociationIdForUser++;
            }
        }
    }

    private static void readAssociationV0(TypedXmlPullParser typedXmlPullParser, int i, int i2, Collection<AssociationInfo> collection) throws XmlPullParserException {
        requireStartOfTag(typedXmlPullParser, XML_TAG_ASSOCIATION);
        String readStringAttribute = XmlUtils.readStringAttribute(typedXmlPullParser, "package");
        String readStringAttribute2 = XmlUtils.readStringAttribute(typedXmlPullParser, LEGACY_XML_ATTR_DEVICE);
        if (readStringAttribute == null || readStringAttribute2 == null) {
            return;
        }
        collection.add(new AssociationInfo(i2, i, readStringAttribute, MacAddress.fromString(readStringAttribute2), null, XmlUtils.readStringAttribute(typedXmlPullParser, XML_ATTR_PROFILE), null, false, XmlUtils.readBooleanAttribute(typedXmlPullParser, XML_ATTR_NOTIFY_DEVICE_NEARBY), false, XmlUtils.readLongAttribute(typedXmlPullParser, XML_ATTR_TIME_APPROVED, 0L), Long.MAX_VALUE, 0));
    }

    private static void readAssociationsV1(TypedXmlPullParser typedXmlPullParser, int i, Collection<AssociationInfo> collection) throws XmlPullParserException, IOException {
        requireStartOfTag(typedXmlPullParser, XML_TAG_ASSOCIATIONS);
        while (true) {
            typedXmlPullParser.nextTag();
            if (DataStoreUtils.isEndOfTag(typedXmlPullParser, XML_TAG_ASSOCIATIONS)) {
                return;
            }
            if (DataStoreUtils.isStartOfTag(typedXmlPullParser, XML_TAG_ASSOCIATION)) {
                readAssociationV1(typedXmlPullParser, i, collection);
            }
        }
    }

    private static void readAssociationV1(TypedXmlPullParser typedXmlPullParser, int i, Collection<AssociationInfo> collection) throws XmlPullParserException, IOException {
        requireStartOfTag(typedXmlPullParser, XML_TAG_ASSOCIATION);
        AssociationInfo createAssociationInfoNoThrow = createAssociationInfoNoThrow(XmlUtils.readIntAttribute(typedXmlPullParser, "id"), i, XmlUtils.readStringAttribute(typedXmlPullParser, "package"), stringToMacAddress(XmlUtils.readStringAttribute(typedXmlPullParser, XML_ATTR_MAC_ADDRESS)), XmlUtils.readStringAttribute(typedXmlPullParser, XML_ATTR_DISPLAY_NAME), XmlUtils.readStringAttribute(typedXmlPullParser, XML_ATTR_PROFILE), XmlUtils.readBooleanAttribute(typedXmlPullParser, XML_ATTR_SELF_MANAGED), XmlUtils.readBooleanAttribute(typedXmlPullParser, XML_ATTR_NOTIFY_DEVICE_NEARBY), XmlUtils.readBooleanAttribute(typedXmlPullParser, XML_ATTR_REVOKED, false), XmlUtils.readLongAttribute(typedXmlPullParser, XML_ATTR_TIME_APPROVED, 0L), XmlUtils.readLongAttribute(typedXmlPullParser, XML_ATTR_LAST_TIME_CONNECTED, Long.MAX_VALUE), XmlUtils.readIntAttribute(typedXmlPullParser, XML_ATTR_SYSTEM_DATA_SYNC_FLAGS, 0));
        if (createAssociationInfoNoThrow != null) {
            collection.add(createAssociationInfoNoThrow);
        }
    }

    private static void readPreviouslyUsedIdsV1(TypedXmlPullParser typedXmlPullParser, Map<String, Set<Integer>> map) throws XmlPullParserException, IOException {
        requireStartOfTag(typedXmlPullParser, XML_TAG_PREVIOUSLY_USED_IDS);
        while (true) {
            typedXmlPullParser.nextTag();
            if (DataStoreUtils.isEndOfTag(typedXmlPullParser, XML_TAG_PREVIOUSLY_USED_IDS)) {
                return;
            }
            if (DataStoreUtils.isStartOfTag(typedXmlPullParser, "package")) {
                String readStringAttribute = XmlUtils.readStringAttribute(typedXmlPullParser, XML_ATTR_PACKAGE_NAME);
                HashSet hashSet = new HashSet();
                while (true) {
                    typedXmlPullParser.nextTag();
                    if (DataStoreUtils.isEndOfTag(typedXmlPullParser, "package")) {
                        break;
                    } else if (DataStoreUtils.isStartOfTag(typedXmlPullParser, "id")) {
                        typedXmlPullParser.nextToken();
                        hashSet.add(Integer.valueOf(Integer.parseInt(typedXmlPullParser.getText())));
                    }
                }
                map.put(readStringAttribute, hashSet);
            }
        }
    }

    private static void writeAssociations(XmlSerializer xmlSerializer, Collection<AssociationInfo> collection) throws IOException {
        XmlSerializer startTag = xmlSerializer.startTag(null, XML_TAG_ASSOCIATIONS);
        Iterator<AssociationInfo> it = collection.iterator();
        while (it.hasNext()) {
            writeAssociation(startTag, it.next());
        }
        startTag.endTag(null, XML_TAG_ASSOCIATIONS);
    }

    private static void writeAssociation(XmlSerializer xmlSerializer, AssociationInfo associationInfo) throws IOException {
        XmlSerializer startTag = xmlSerializer.startTag(null, XML_TAG_ASSOCIATION);
        XmlUtils.writeIntAttribute(startTag, "id", associationInfo.getId());
        XmlUtils.writeStringAttribute(startTag, XML_ATTR_PROFILE, associationInfo.getDeviceProfile());
        XmlUtils.writeStringAttribute(startTag, "package", associationInfo.getPackageName());
        XmlUtils.writeStringAttribute(startTag, XML_ATTR_MAC_ADDRESS, associationInfo.getDeviceMacAddressAsString());
        XmlUtils.writeStringAttribute(startTag, XML_ATTR_DISPLAY_NAME, associationInfo.getDisplayName());
        XmlUtils.writeBooleanAttribute(startTag, XML_ATTR_SELF_MANAGED, associationInfo.isSelfManaged());
        XmlUtils.writeBooleanAttribute(startTag, XML_ATTR_NOTIFY_DEVICE_NEARBY, associationInfo.isNotifyOnDeviceNearby());
        XmlUtils.writeBooleanAttribute(startTag, XML_ATTR_REVOKED, associationInfo.isRevoked());
        XmlUtils.writeLongAttribute(startTag, XML_ATTR_TIME_APPROVED, associationInfo.getTimeApprovedMs());
        XmlUtils.writeLongAttribute(startTag, XML_ATTR_LAST_TIME_CONNECTED, associationInfo.getLastTimeConnectedMs().longValue());
        XmlUtils.writeIntAttribute(startTag, XML_ATTR_SYSTEM_DATA_SYNC_FLAGS, associationInfo.getSystemDataSyncFlags());
        startTag.endTag(null, XML_TAG_ASSOCIATION);
    }

    private static void writePreviouslyUsedIds(XmlSerializer xmlSerializer, Map<String, Set<Integer>> map) throws IOException {
        XmlSerializer startTag = xmlSerializer.startTag(null, XML_TAG_PREVIOUSLY_USED_IDS);
        for (Map.Entry<String, Set<Integer>> entry : map.entrySet()) {
            writePreviouslyUsedIdsForPackage(startTag, entry.getKey(), entry.getValue());
        }
        startTag.endTag(null, XML_TAG_PREVIOUSLY_USED_IDS);
    }

    private static void writePreviouslyUsedIdsForPackage(XmlSerializer xmlSerializer, String str, Set<Integer> set) throws IOException {
        final XmlSerializer startTag = xmlSerializer.startTag(null, "package");
        XmlUtils.writeStringAttribute(startTag, XML_ATTR_PACKAGE_NAME, str);
        CollectionUtils.forEach(set, new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.companion.PersistentDataStore$$ExternalSyntheticLambda1
            public final void acceptOrThrow(Object obj) {
                PersistentDataStore.lambda$writePreviouslyUsedIdsForPackage$2(startTag, (Integer) obj);
            }
        });
        startTag.endTag(null, "package");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$writePreviouslyUsedIdsForPackage$2(XmlSerializer xmlSerializer, Integer num) throws Exception {
        xmlSerializer.startTag(null, "id").text(Integer.toString(num.intValue())).endTag(null, "id");
    }

    private static void requireStartOfTag(XmlPullParser xmlPullParser, String str) throws XmlPullParserException {
        if (!DataStoreUtils.isStartOfTag(xmlPullParser, str)) {
            throw new XmlPullParserException("Should be at the start of \"associations\" tag");
        }
    }

    private static MacAddress stringToMacAddress(String str) {
        if (str != null) {
            return MacAddress.fromString(str);
        }
        return null;
    }

    private static AssociationInfo createAssociationInfoNoThrow(int i, int i2, String str, MacAddress macAddress, CharSequence charSequence, String str2, boolean z, boolean z2, boolean z3, long j, long j2, int i3) {
        try {
            return new AssociationInfo(i, i2, str, macAddress, charSequence, str2, null, z, z2, z3, j, j2, i3);
        } catch (Exception unused) {
            return null;
        }
    }
}
