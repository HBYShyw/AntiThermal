package com.android.server.people.data;

import android.content.LocusId;
import android.os.FileUtils;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.android.server.cpu.CpuInfoReader$;
import java.io.File;
import java.io.FileFilter;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageData {
    private final ConversationStore mConversationStore;
    private final EventStore mEventStore;
    private final Predicate<String> mIsDefaultDialerPredicate;
    private final Predicate<String> mIsDefaultSmsAppPredicate;
    private final File mPackageDataDir;
    private final String mPackageName;
    private final int mUserId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageData(String str, int i, Predicate<String> predicate, Predicate<String> predicate2, ScheduledExecutorService scheduledExecutorService, File file) {
        this.mPackageName = str;
        this.mUserId = i;
        File file2 = new File(file, str);
        this.mPackageDataDir = file2;
        file2.mkdirs();
        this.mConversationStore = new ConversationStore(file2, scheduledExecutorService);
        this.mEventStore = new EventStore(file2, scheduledExecutorService);
        this.mIsDefaultDialerPredicate = predicate;
        this.mIsDefaultSmsAppPredicate = predicate2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Map<String, PackageData> packagesDataFromDisk(int i, Predicate<String> predicate, Predicate<String> predicate2, ScheduledExecutorService scheduledExecutorService, File file) {
        ArrayMap arrayMap = new ArrayMap();
        File[] listFiles = file.listFiles((FileFilter) new CpuInfoReader$.ExternalSyntheticLambda2());
        if (listFiles == null) {
            return arrayMap;
        }
        for (File file2 : listFiles) {
            PackageData packageData = new PackageData(file2.getName(), i, predicate, predicate2, scheduledExecutorService, file);
            packageData.loadFromDisk();
            arrayMap.put(file2.getName(), packageData);
        }
        return arrayMap;
    }

    private void loadFromDisk() {
        this.mConversationStore.loadConversationsFromDisk();
        this.mEventStore.loadFromDisk();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveToDisk() {
        this.mConversationStore.saveConversationsToDisk();
        this.mEventStore.saveToDisk();
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public void forAllConversations(Consumer<ConversationInfo> consumer) {
        this.mConversationStore.forAllConversations(consumer);
    }

    public ConversationInfo getConversationInfo(String str) {
        return getConversationStore().getConversation(str);
    }

    public EventHistory getEventHistory(String str) {
        EventHistory eventHistory;
        EventHistory eventHistory2;
        EventHistory eventHistory3;
        AggregateEventHistoryImpl aggregateEventHistoryImpl = new AggregateEventHistoryImpl();
        ConversationInfo conversation = this.mConversationStore.getConversation(str);
        if (conversation == null) {
            return aggregateEventHistoryImpl;
        }
        EventHistory eventHistory4 = getEventStore().getEventHistory(0, str);
        if (eventHistory4 != null) {
            aggregateEventHistoryImpl.addEventHistory(eventHistory4);
        }
        LocusId locusId = conversation.getLocusId();
        if (locusId != null && (eventHistory3 = getEventStore().getEventHistory(1, locusId.getId())) != null) {
            aggregateEventHistoryImpl.addEventHistory(eventHistory3);
        }
        String contactPhoneNumber = conversation.getContactPhoneNumber();
        if (TextUtils.isEmpty(contactPhoneNumber)) {
            return aggregateEventHistoryImpl;
        }
        if (isDefaultDialer() && (eventHistory2 = getEventStore().getEventHistory(2, contactPhoneNumber)) != null) {
            aggregateEventHistoryImpl.addEventHistory(eventHistory2);
        }
        if (isDefaultSmsApp() && (eventHistory = getEventStore().getEventHistory(3, contactPhoneNumber)) != null) {
            aggregateEventHistoryImpl.addEventHistory(eventHistory);
        }
        return aggregateEventHistoryImpl;
    }

    public EventHistory getClassLevelEventHistory(String str) {
        EventHistory eventHistory = getEventStore().getEventHistory(4, str);
        return eventHistory != null ? eventHistory : new AggregateEventHistoryImpl();
    }

    public boolean isDefaultDialer() {
        return this.mIsDefaultDialerPredicate.test(this.mPackageName);
    }

    public boolean isDefaultSmsApp() {
        return this.mIsDefaultSmsAppPredicate.test(this.mPackageName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConversationStore getConversationStore() {
        return this.mConversationStore;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EventStore getEventStore() {
        return this.mEventStore;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deleteDataForConversation(String str) {
        ConversationInfo deleteConversation = this.mConversationStore.deleteConversation(str);
        if (deleteConversation == null) {
            return;
        }
        this.mEventStore.deleteEventHistory(0, str);
        if (deleteConversation.getLocusId() != null) {
            this.mEventStore.deleteEventHistory(1, deleteConversation.getLocusId().getId());
        }
        String contactPhoneNumber = deleteConversation.getContactPhoneNumber();
        if (TextUtils.isEmpty(contactPhoneNumber)) {
            return;
        }
        if (isDefaultDialer()) {
            this.mEventStore.deleteEventHistory(2, contactPhoneNumber);
        }
        if (isDefaultSmsApp()) {
            this.mEventStore.deleteEventHistory(3, contactPhoneNumber);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pruneOrphanEvents() {
        this.mEventStore.pruneOrphanEventHistories(0, new Predicate() { // from class: com.android.server.people.data.PackageData$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$pruneOrphanEvents$0;
                lambda$pruneOrphanEvents$0 = PackageData.this.lambda$pruneOrphanEvents$0((String) obj);
                return lambda$pruneOrphanEvents$0;
            }
        });
        this.mEventStore.pruneOrphanEventHistories(1, new Predicate() { // from class: com.android.server.people.data.PackageData$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$pruneOrphanEvents$1;
                lambda$pruneOrphanEvents$1 = PackageData.this.lambda$pruneOrphanEvents$1((String) obj);
                return lambda$pruneOrphanEvents$1;
            }
        });
        if (isDefaultDialer()) {
            this.mEventStore.pruneOrphanEventHistories(2, new Predicate() { // from class: com.android.server.people.data.PackageData$$ExternalSyntheticLambda2
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$pruneOrphanEvents$2;
                    lambda$pruneOrphanEvents$2 = PackageData.this.lambda$pruneOrphanEvents$2((String) obj);
                    return lambda$pruneOrphanEvents$2;
                }
            });
        }
        if (isDefaultSmsApp()) {
            this.mEventStore.pruneOrphanEventHistories(3, new Predicate() { // from class: com.android.server.people.data.PackageData$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$pruneOrphanEvents$3;
                    lambda$pruneOrphanEvents$3 = PackageData.this.lambda$pruneOrphanEvents$3((String) obj);
                    return lambda$pruneOrphanEvents$3;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$pruneOrphanEvents$0(String str) {
        return this.mConversationStore.getConversation(str) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$pruneOrphanEvents$1(String str) {
        return this.mConversationStore.getConversationByLocusId(new LocusId(str)) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$pruneOrphanEvents$2(String str) {
        return this.mConversationStore.getConversationByPhoneNumber(str) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$pruneOrphanEvents$3(String str) {
        return this.mConversationStore.getConversationByPhoneNumber(str) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDestroy() {
        this.mEventStore.onDestroy();
        this.mConversationStore.onDestroy();
        FileUtils.deleteContentsAndDir(this.mPackageDataDir);
    }
}
