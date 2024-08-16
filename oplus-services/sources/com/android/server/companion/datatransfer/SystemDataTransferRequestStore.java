package com.android.server.companion.datatransfer;

import android.companion.datatransfer.PermissionSyncRequest;
import android.companion.datatransfer.SystemDataTransferRequest;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.FunctionalUtils;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.companion.DataStoreUtils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Predicate;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SystemDataTransferRequestStore {
    private static final String FILE_NAME = "companion_device_system_data_transfer_requests.xml";
    private static final String LOG_TAG = "CDM_SystemDataTransferRequestStore";
    private static final int READ_FROM_DISK_TIMEOUT = 5;
    private static final String XML_ATTR_ASSOCIATION_ID = "association_id";
    private static final String XML_ATTR_DATA_TYPE = "data_type";
    private static final String XML_ATTR_IS_USER_CONSENTED = "is_user_consented";
    private static final String XML_ATTR_USER_ID = "user_id";
    private static final String XML_TAG_REQUEST = "request";
    private static final String XML_TAG_REQUESTS = "requests";
    private final ConcurrentMap<Integer, AtomicFile> mUserIdToStorageFile = new ConcurrentHashMap();
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final SparseArray<ArrayList<SystemDataTransferRequest>> mCachedPerUser = new SparseArray<>();
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<SystemDataTransferRequest> readRequestsByAssociationId(int i, int i2) {
        ArrayList<SystemDataTransferRequest> readRequestsFromCache;
        synchronized (this.mLock) {
            readRequestsFromCache = readRequestsFromCache(i);
        }
        ArrayList arrayList = new ArrayList();
        for (SystemDataTransferRequest systemDataTransferRequest : readRequestsFromCache) {
            if (systemDataTransferRequest.getAssociationId() == i2) {
                arrayList.add(systemDataTransferRequest);
            }
        }
        return arrayList;
    }

    public void writeRequest(final int i, final SystemDataTransferRequest systemDataTransferRequest) {
        final ArrayList<SystemDataTransferRequest> readRequestsFromCache;
        Slog.i(LOG_TAG, "Writing request=" + systemDataTransferRequest + " to store.");
        synchronized (this.mLock) {
            readRequestsFromCache = readRequestsFromCache(i);
            readRequestsFromCache.removeIf(new Predicate() { // from class: com.android.server.companion.datatransfer.SystemDataTransferRequestStore$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$writeRequest$0;
                    lambda$writeRequest$0 = SystemDataTransferRequestStore.lambda$writeRequest$0(systemDataTransferRequest, (SystemDataTransferRequest) obj);
                    return lambda$writeRequest$0;
                }
            });
            readRequestsFromCache.add(systemDataTransferRequest);
            this.mCachedPerUser.set(i, readRequestsFromCache);
        }
        this.mExecutor.execute(new Runnable() { // from class: com.android.server.companion.datatransfer.SystemDataTransferRequestStore$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SystemDataTransferRequestStore.this.lambda$writeRequest$1(i, readRequestsFromCache);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$writeRequest$0(SystemDataTransferRequest systemDataTransferRequest, SystemDataTransferRequest systemDataTransferRequest2) {
        return systemDataTransferRequest2.getAssociationId() == systemDataTransferRequest.getAssociationId();
    }

    public void removeRequestsByAssociationId(final int i, final int i2) {
        final ArrayList<SystemDataTransferRequest> readRequestsFromCache;
        Slog.i(LOG_TAG, "Removing system data transfer requests for userId=" + i + ", associationId=" + i2);
        synchronized (this.mLock) {
            readRequestsFromCache = readRequestsFromCache(i);
            readRequestsFromCache.removeIf(new Predicate() { // from class: com.android.server.companion.datatransfer.SystemDataTransferRequestStore$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$removeRequestsByAssociationId$2;
                    lambda$removeRequestsByAssociationId$2 = SystemDataTransferRequestStore.lambda$removeRequestsByAssociationId$2(i2, (SystemDataTransferRequest) obj);
                    return lambda$removeRequestsByAssociationId$2;
                }
            });
            this.mCachedPerUser.set(i, readRequestsFromCache);
        }
        this.mExecutor.execute(new Runnable() { // from class: com.android.server.companion.datatransfer.SystemDataTransferRequestStore$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                SystemDataTransferRequestStore.this.lambda$removeRequestsByAssociationId$3(i, readRequestsFromCache);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$removeRequestsByAssociationId$2(int i, SystemDataTransferRequest systemDataTransferRequest) {
        return systemDataTransferRequest.getAssociationId() == i;
    }

    @GuardedBy({"mLock"})
    private ArrayList<SystemDataTransferRequest> readRequestsFromCache(final int i) {
        ArrayList<SystemDataTransferRequest> arrayList = this.mCachedPerUser.get(i);
        if (arrayList == null) {
            try {
                arrayList = (ArrayList) this.mExecutor.submit(new Callable() { // from class: com.android.server.companion.datatransfer.SystemDataTransferRequestStore$$ExternalSyntheticLambda6
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        ArrayList lambda$readRequestsFromCache$4;
                        lambda$readRequestsFromCache$4 = SystemDataTransferRequestStore.this.lambda$readRequestsFromCache$4(i);
                        return lambda$readRequestsFromCache$4;
                    }
                }).get(5L, TimeUnit.SECONDS);
            } catch (InterruptedException unused) {
                Slog.e(LOG_TAG, "Thread reading SystemDataTransferRequest from disk is interrupted.");
            } catch (ExecutionException unused2) {
                Slog.e(LOG_TAG, "Error occurred while reading SystemDataTransferRequest from disk.");
            } catch (TimeoutException unused3) {
                Slog.e(LOG_TAG, "Reading SystemDataTransferRequest from disk timed out.");
            }
            this.mCachedPerUser.set(i, arrayList);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: readRequestsFromStore, reason: merged with bridge method [inline-methods] */
    public ArrayList<SystemDataTransferRequest> lambda$readRequestsFromCache$4(int i) {
        AtomicFile storageFileForUser = getStorageFileForUser(i);
        Slog.i(LOG_TAG, "Reading SystemDataTransferRequests for user " + i + " from file=" + storageFileForUser.getBaseFile().getPath());
        synchronized (storageFileForUser) {
            if (!storageFileForUser.getBaseFile().exists()) {
                Slog.d(LOG_TAG, "File does not exist -> Abort");
                return new ArrayList<>();
            }
            try {
                FileInputStream openRead = storageFileForUser.openRead();
                try {
                    TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(openRead);
                    XmlUtils.beginDocument(resolvePullParser, XML_TAG_REQUESTS);
                    ArrayList<SystemDataTransferRequest> readRequestsFromXml = readRequestsFromXml(resolvePullParser);
                    if (openRead != null) {
                        openRead.close();
                    }
                    return readRequestsFromXml;
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
                Slog.e(LOG_TAG, "Error while reading requests file", e);
                return new ArrayList<>();
            }
        }
    }

    private ArrayList<SystemDataTransferRequest> readRequestsFromXml(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        if (!DataStoreUtils.isStartOfTag(typedXmlPullParser, XML_TAG_REQUESTS)) {
            throw new XmlPullParserException("The XML doesn't have start tag: requests");
        }
        ArrayList<SystemDataTransferRequest> arrayList = new ArrayList<>();
        while (true) {
            typedXmlPullParser.nextTag();
            if (DataStoreUtils.isEndOfTag(typedXmlPullParser, XML_TAG_REQUESTS)) {
                return arrayList;
            }
            if (DataStoreUtils.isStartOfTag(typedXmlPullParser, XML_TAG_REQUEST)) {
                arrayList.add(readRequestFromXml(typedXmlPullParser));
            }
        }
    }

    private SystemDataTransferRequest readRequestFromXml(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        if (!DataStoreUtils.isStartOfTag(typedXmlPullParser, XML_TAG_REQUEST)) {
            throw new XmlPullParserException("XML doesn't have start tag: request");
        }
        int readIntAttribute = XmlUtils.readIntAttribute(typedXmlPullParser, XML_ATTR_ASSOCIATION_ID);
        int readIntAttribute2 = XmlUtils.readIntAttribute(typedXmlPullParser, XML_ATTR_DATA_TYPE);
        int readIntAttribute3 = XmlUtils.readIntAttribute(typedXmlPullParser, XML_ATTR_USER_ID);
        boolean readBooleanAttribute = XmlUtils.readBooleanAttribute(typedXmlPullParser, XML_ATTR_IS_USER_CONSENTED);
        if (readIntAttribute2 != 1) {
            return null;
        }
        PermissionSyncRequest permissionSyncRequest = new PermissionSyncRequest(readIntAttribute);
        permissionSyncRequest.setUserId(readIntAttribute3);
        permissionSyncRequest.setUserConsented(readBooleanAttribute);
        return permissionSyncRequest;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: writeRequestsToStore, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$writeRequest$1(int i, final List<SystemDataTransferRequest> list) {
        AtomicFile storageFileForUser = getStorageFileForUser(i);
        Slog.i(LOG_TAG, "Writing SystemDataTransferRequests for user " + i + " to file=" + storageFileForUser.getBaseFile().getPath());
        synchronized (storageFileForUser) {
            DataStoreUtils.writeToFileSafely(storageFileForUser, new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.companion.datatransfer.SystemDataTransferRequestStore$$ExternalSyntheticLambda3
                public final void acceptOrThrow(Object obj) {
                    SystemDataTransferRequestStore.this.lambda$writeRequestsToStore$5(list, (FileOutputStream) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$writeRequestsToStore$5(List list, FileOutputStream fileOutputStream) throws Exception {
        TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(fileOutputStream);
        resolveSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        resolveSerializer.startDocument((String) null, Boolean.TRUE);
        writeRequestsToXml(resolveSerializer, list);
        resolveSerializer.endDocument();
    }

    private void writeRequestsToXml(TypedXmlSerializer typedXmlSerializer, Collection<SystemDataTransferRequest> collection) throws IOException {
        typedXmlSerializer.startTag((String) null, XML_TAG_REQUESTS);
        Iterator<SystemDataTransferRequest> it = collection.iterator();
        while (it.hasNext()) {
            writeRequestToXml(typedXmlSerializer, it.next());
        }
        typedXmlSerializer.endTag((String) null, XML_TAG_REQUESTS);
    }

    private void writeRequestToXml(TypedXmlSerializer typedXmlSerializer, SystemDataTransferRequest systemDataTransferRequest) throws IOException {
        typedXmlSerializer.startTag((String) null, XML_TAG_REQUEST);
        XmlUtils.writeIntAttribute(typedXmlSerializer, XML_ATTR_ASSOCIATION_ID, systemDataTransferRequest.getAssociationId());
        XmlUtils.writeIntAttribute(typedXmlSerializer, XML_ATTR_DATA_TYPE, systemDataTransferRequest.getDataType());
        XmlUtils.writeIntAttribute(typedXmlSerializer, XML_ATTR_USER_ID, systemDataTransferRequest.getUserId());
        XmlUtils.writeBooleanAttribute(typedXmlSerializer, XML_ATTR_IS_USER_CONSENTED, systemDataTransferRequest.isUserConsented());
        typedXmlSerializer.endTag((String) null, XML_TAG_REQUEST);
    }

    private AtomicFile getStorageFileForUser(final int i) {
        return this.mUserIdToStorageFile.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.server.companion.datatransfer.SystemDataTransferRequestStore$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                AtomicFile createStorageFileForUser;
                createStorageFileForUser = DataStoreUtils.createStorageFileForUser(i, SystemDataTransferRequestStore.FILE_NAME);
                return createStorageFileForUser;
            }
        });
    }
}
