package com.android.server.people.data;

import android.content.LocusId;
import android.net.Uri;
import android.util.ArrayMap;
import android.util.Slog;
import android.util.proto.ProtoInputStream;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.server.people.data.AbstractProtoDiskReadWriter;
import com.android.server.people.data.ConversationStore;
import com.google.android.collect.Lists;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ConversationStore {
    private static final String CONVERSATIONS_FILE_NAME = "conversations";
    private static final int CONVERSATION_INFOS_END_TOKEN = -1;
    private static final String TAG = "ConversationStore";
    private ConversationInfosProtoDiskReadWriter mConversationInfosProtoDiskReadWriter;
    private final File mPackageDir;
    private final ScheduledExecutorService mScheduledExecutorService;

    @GuardedBy({"this"})
    private final Map<String, ConversationInfo> mConversationInfoMap = new ArrayMap();

    @GuardedBy({"this"})
    private final Map<LocusId, String> mLocusIdToShortcutIdMap = new ArrayMap();

    @GuardedBy({"this"})
    private final Map<Uri, String> mContactUriToShortcutIdMap = new ArrayMap();

    @GuardedBy({"this"})
    private final Map<String, String> mPhoneNumberToShortcutIdMap = new ArrayMap();

    @GuardedBy({"this"})
    private final Map<String, String> mNotifChannelIdToShortcutIdMap = new ArrayMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConversationStore(File file, ScheduledExecutorService scheduledExecutorService) {
        this.mScheduledExecutorService = scheduledExecutorService;
        this.mPackageDir = file;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void loadConversationsFromDisk() {
        List<ConversationInfo> read;
        ConversationInfosProtoDiskReadWriter conversationInfosProtoDiskReadWriter = getConversationInfosProtoDiskReadWriter();
        if (conversationInfosProtoDiskReadWriter == null || (read = conversationInfosProtoDiskReadWriter.read(CONVERSATIONS_FILE_NAME)) == null) {
            return;
        }
        Iterator<ConversationInfo> it = read.iterator();
        while (it.hasNext()) {
            updateConversationsInMemory(it.next());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveConversationsToDisk() {
        ArrayList arrayList;
        ConversationInfosProtoDiskReadWriter conversationInfosProtoDiskReadWriter = getConversationInfosProtoDiskReadWriter();
        if (conversationInfosProtoDiskReadWriter != null) {
            synchronized (this) {
                arrayList = new ArrayList(this.mConversationInfoMap.values());
            }
            conversationInfosProtoDiskReadWriter.saveConversationsImmediately(arrayList);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addOrUpdate(ConversationInfo conversationInfo) {
        updateConversationsInMemory(conversationInfo);
        scheduleUpdateConversationsOnDisk();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConversationInfo deleteConversation(String str) {
        synchronized (this) {
            ConversationInfo remove = this.mConversationInfoMap.remove(str);
            if (remove == null) {
                return null;
            }
            LocusId locusId = remove.getLocusId();
            if (locusId != null) {
                this.mLocusIdToShortcutIdMap.remove(locusId);
            }
            Uri contactUri = remove.getContactUri();
            if (contactUri != null) {
                this.mContactUriToShortcutIdMap.remove(contactUri);
            }
            String contactPhoneNumber = remove.getContactPhoneNumber();
            if (contactPhoneNumber != null) {
                this.mPhoneNumberToShortcutIdMap.remove(contactPhoneNumber);
            }
            String notificationChannelId = remove.getNotificationChannelId();
            if (notificationChannelId != null) {
                this.mNotifChannelIdToShortcutIdMap.remove(notificationChannelId);
            }
            scheduleUpdateConversationsOnDisk();
            return remove;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forAllConversations(Consumer<ConversationInfo> consumer) {
        ArrayList arrayList;
        synchronized (this) {
            arrayList = new ArrayList(this.mConversationInfoMap.values());
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            consumer.accept((ConversationInfo) it.next());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ConversationInfo getConversation(String str) {
        return str != null ? this.mConversationInfoMap.get(str) : null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ConversationInfo getConversationByLocusId(LocusId locusId) {
        return getConversation(this.mLocusIdToShortcutIdMap.get(locusId));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ConversationInfo getConversationByContactUri(Uri uri) {
        return getConversation(this.mContactUriToShortcutIdMap.get(uri));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ConversationInfo getConversationByPhoneNumber(String str) {
        return getConversation(this.mPhoneNumberToShortcutIdMap.get(str));
    }

    synchronized ConversationInfo getConversationByNotificationChannelId(String str) {
        return getConversation(this.mNotifChannelIdToShortcutIdMap.get(str));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDestroy() {
        synchronized (this) {
            this.mConversationInfoMap.clear();
            this.mContactUriToShortcutIdMap.clear();
            this.mLocusIdToShortcutIdMap.clear();
            this.mNotifChannelIdToShortcutIdMap.clear();
            this.mPhoneNumberToShortcutIdMap.clear();
        }
        ConversationInfosProtoDiskReadWriter conversationInfosProtoDiskReadWriter = getConversationInfosProtoDiskReadWriter();
        if (conversationInfosProtoDiskReadWriter != null) {
            conversationInfosProtoDiskReadWriter.deleteConversationsFile();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] getBackupPayload() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        forAllConversations(new Consumer() { // from class: com.android.server.people.data.ConversationStore$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ConversationStore.lambda$getBackupPayload$0(dataOutputStream, (ConversationInfo) obj);
            }
        });
        try {
            dataOutputStream.writeInt(-1);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            Slog.e(TAG, "Failed to write conversation infos end token to backup payload.", e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getBackupPayload$0(DataOutputStream dataOutputStream, ConversationInfo conversationInfo) {
        byte[] backupPayload = conversationInfo.getBackupPayload();
        if (backupPayload == null) {
            return;
        }
        try {
            dataOutputStream.writeInt(backupPayload.length);
            dataOutputStream.write(backupPayload);
        } catch (IOException e) {
            Slog.e(TAG, "Failed to write conversation info to backup payload.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restore(byte[] bArr) {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        try {
            for (int readInt = dataInputStream.readInt(); readInt != -1; readInt = dataInputStream.readInt()) {
                byte[] bArr2 = new byte[readInt];
                dataInputStream.readFully(bArr2, 0, readInt);
                ConversationInfo readFromBackupPayload = ConversationInfo.readFromBackupPayload(bArr2);
                if (readFromBackupPayload != null) {
                    addOrUpdate(readFromBackupPayload);
                }
            }
        } catch (IOException e) {
            Slog.e(TAG, "Failed to read conversation info from payload.", e);
        }
    }

    private synchronized void updateConversationsInMemory(ConversationInfo conversationInfo) {
        this.mConversationInfoMap.put(conversationInfo.getShortcutId(), conversationInfo);
        LocusId locusId = conversationInfo.getLocusId();
        if (locusId != null) {
            this.mLocusIdToShortcutIdMap.put(locusId, conversationInfo.getShortcutId());
        }
        Uri contactUri = conversationInfo.getContactUri();
        if (contactUri != null) {
            this.mContactUriToShortcutIdMap.put(contactUri, conversationInfo.getShortcutId());
        }
        String contactPhoneNumber = conversationInfo.getContactPhoneNumber();
        if (contactPhoneNumber != null) {
            this.mPhoneNumberToShortcutIdMap.put(contactPhoneNumber, conversationInfo.getShortcutId());
        }
        String notificationChannelId = conversationInfo.getNotificationChannelId();
        if (notificationChannelId != null) {
            this.mNotifChannelIdToShortcutIdMap.put(notificationChannelId, conversationInfo.getShortcutId());
        }
    }

    private void scheduleUpdateConversationsOnDisk() {
        ArrayList arrayList;
        ConversationInfosProtoDiskReadWriter conversationInfosProtoDiskReadWriter = getConversationInfosProtoDiskReadWriter();
        if (conversationInfosProtoDiskReadWriter != null) {
            synchronized (this) {
                arrayList = new ArrayList(this.mConversationInfoMap.values());
            }
            conversationInfosProtoDiskReadWriter.scheduleConversationsSave(arrayList);
        }
    }

    private ConversationInfosProtoDiskReadWriter getConversationInfosProtoDiskReadWriter() {
        if (!this.mPackageDir.exists()) {
            Slog.e(TAG, "Package data directory does not exist: " + this.mPackageDir.getAbsolutePath());
            return null;
        }
        if (this.mConversationInfosProtoDiskReadWriter == null) {
            this.mConversationInfosProtoDiskReadWriter = new ConversationInfosProtoDiskReadWriter(this.mPackageDir, CONVERSATIONS_FILE_NAME, this.mScheduledExecutorService);
        }
        return this.mConversationInfosProtoDiskReadWriter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ConversationInfosProtoDiskReadWriter extends AbstractProtoDiskReadWriter<List<ConversationInfo>> {
        private final String mConversationInfoFileName;

        ConversationInfosProtoDiskReadWriter(File file, String str, ScheduledExecutorService scheduledExecutorService) {
            super(file, scheduledExecutorService);
            this.mConversationInfoFileName = str;
        }

        @Override // com.android.server.people.data.AbstractProtoDiskReadWriter
        AbstractProtoDiskReadWriter.ProtoStreamWriter<List<ConversationInfo>> protoStreamWriter() {
            return new AbstractProtoDiskReadWriter.ProtoStreamWriter() { // from class: com.android.server.people.data.ConversationStore$ConversationInfosProtoDiskReadWriter$$ExternalSyntheticLambda0
                @Override // com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamWriter
                public final void write(ProtoOutputStream protoOutputStream, Object obj) {
                    ConversationStore.ConversationInfosProtoDiskReadWriter.lambda$protoStreamWriter$0(protoOutputStream, (List) obj);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$protoStreamWriter$0(ProtoOutputStream protoOutputStream, List list) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                ConversationInfo conversationInfo = (ConversationInfo) it.next();
                long start = protoOutputStream.start(2246267895809L);
                conversationInfo.writeToProto(protoOutputStream);
                protoOutputStream.end(start);
            }
        }

        @Override // com.android.server.people.data.AbstractProtoDiskReadWriter
        AbstractProtoDiskReadWriter.ProtoStreamReader<List<ConversationInfo>> protoStreamReader() {
            return new AbstractProtoDiskReadWriter.ProtoStreamReader() { // from class: com.android.server.people.data.ConversationStore$ConversationInfosProtoDiskReadWriter$$ExternalSyntheticLambda1
                @Override // com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamReader
                public final Object read(ProtoInputStream protoInputStream) {
                    List lambda$protoStreamReader$1;
                    lambda$protoStreamReader$1 = ConversationStore.ConversationInfosProtoDiskReadWriter.lambda$protoStreamReader$1(protoInputStream);
                    return lambda$protoStreamReader$1;
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ List lambda$protoStreamReader$1(ProtoInputStream protoInputStream) {
            ArrayList newArrayList = Lists.newArrayList();
            while (protoInputStream.nextField() != -1) {
                try {
                    if (protoInputStream.getFieldNumber() == 1) {
                        long start = protoInputStream.start(2246267895809L);
                        ConversationInfo readFromProto = ConversationInfo.readFromProto(protoInputStream);
                        protoInputStream.end(start);
                        newArrayList.add(readFromProto);
                    }
                } catch (IOException e) {
                    Slog.e(ConversationStore.TAG, "Failed to read protobuf input stream.", e);
                }
            }
            return newArrayList;
        }

        void scheduleConversationsSave(List<ConversationInfo> list) {
            scheduleSave(this.mConversationInfoFileName, list);
        }

        void saveConversationsImmediately(List<ConversationInfo> list) {
            saveImmediately(this.mConversationInfoFileName, list);
        }

        void deleteConversationsFile() {
            delete(this.mConversationInfoFileName);
        }
    }
}
