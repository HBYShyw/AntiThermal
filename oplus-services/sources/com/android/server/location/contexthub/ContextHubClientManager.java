package com.android.server.location.contexthub;

import android.app.PendingIntent;
import android.content.Context;
import android.hardware.location.ContextHubInfo;
import android.hardware.location.IContextHubClient;
import android.hardware.location.IContextHubClientCallback;
import android.hardware.location.NanoAppMessage;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.util.proto.ProtoOutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ContextHubClientManager {
    public static final int ACTION_CANCELLED = 2;
    public static final int ACTION_REGISTERED = 0;
    public static final int ACTION_UNREGISTERED = 1;
    private static final boolean DEBUG_LOG_ENABLED = false;
    private static final int MAX_CLIENT_ID = 32767;
    private static final int NUM_CLIENT_RECORDS = 20;
    private static final String TAG = "ContextHubClientManager";
    private final Context mContext;
    private final IContextHubWrapper mContextHubProxy;
    private final ConcurrentHashMap<Short, ContextHubClientBroker> mHostEndPointIdToClientMap = new ConcurrentHashMap<>();
    private int mNextHostEndPointId = 0;
    private final ConcurrentLinkedEvictingDeque<RegistrationRecord> mRegistrationRecordDeque = new ConcurrentLinkedEvictingDeque<>(20);

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface Action {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class RegistrationRecord {
        private final int mAction;
        private final String mBroker;
        private final long mTimestamp = System.currentTimeMillis();

        RegistrationRecord(String str, int i) {
            this.mBroker = str;
            this.mAction = i;
        }

        void dump(ProtoOutputStream protoOutputStream) {
            protoOutputStream.write(1112396529665L, this.mTimestamp);
            protoOutputStream.write(1120986464258L, this.mAction);
            protoOutputStream.write(1138166333443L, this.mBroker);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(ContextHubServiceUtil.formatDateFromTimestamp(this.mTimestamp));
            sb.append(" ");
            sb.append(this.mAction == 0 ? "+ " : "- ");
            sb.append(this.mBroker);
            if (this.mAction == 2) {
                sb.append(" (cancelled)");
            }
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContextHubClientManager(Context context, IContextHubWrapper iContextHubWrapper) {
        this.mContext = context;
        this.mContextHubProxy = iContextHubWrapper;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v0, types: [com.android.server.location.contexthub.ContextHubClientBroker, java.lang.Object, android.os.IBinder] */
    public IContextHubClient registerClient(ContextHubInfo contextHubInfo, IContextHubClientCallback iContextHubClientCallback, String str, ContextHubTransactionManager contextHubTransactionManager, String str2) {
        ?? contextHubClientBroker;
        synchronized (this) {
            short hostEndPointId = getHostEndPointId();
            contextHubClientBroker = new ContextHubClientBroker(this.mContext, this.mContextHubProxy, this, contextHubInfo, hostEndPointId, iContextHubClientCallback, str, contextHubTransactionManager, str2);
            this.mHostEndPointIdToClientMap.put(Short.valueOf(hostEndPointId), contextHubClientBroker);
            this.mRegistrationRecordDeque.add(new RegistrationRecord(contextHubClientBroker.toString(), 0));
        }
        try {
            contextHubClientBroker.attachDeathRecipient();
            Log.d(TAG, "Registered client with host endpoint ID " + ((int) contextHubClientBroker.getHostEndPointId()));
            return IContextHubClient.Stub.asInterface((IBinder) contextHubClientBroker);
        } catch (RemoteException unused) {
            Log.e(TAG, "Failed to attach death recipient to client");
            contextHubClientBroker.close();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.server.location.contexthub.ContextHubClientBroker, android.os.IBinder] */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r1v7 */
    public IContextHubClient registerClient(ContextHubInfo contextHubInfo, PendingIntent pendingIntent, long j, String str, ContextHubTransactionManager contextHubTransactionManager) {
        ?? r1;
        String str2 = "Regenerated";
        synchronized (this) {
            ContextHubClientBroker clientBroker = getClientBroker(contextHubInfo.getId(), pendingIntent, j);
            if (clientBroker == null) {
                short hostEndPointId = getHostEndPointId();
                ContextHubClientBroker contextHubClientBroker = new ContextHubClientBroker(this.mContext, this.mContextHubProxy, this, contextHubInfo, hostEndPointId, pendingIntent, j, str, contextHubTransactionManager);
                this.mHostEndPointIdToClientMap.put(Short.valueOf(hostEndPointId), contextHubClientBroker);
                str2 = "Registered";
                this.mRegistrationRecordDeque.add(new RegistrationRecord(contextHubClientBroker.toString(), 0));
                r1 = contextHubClientBroker;
            } else {
                clientBroker.setAttributionTag(str);
                r1 = clientBroker;
            }
        }
        Log.d(TAG, str2 + " client with host endpoint ID " + ((int) r1.getHostEndPointId()));
        return IContextHubClient.Stub.asInterface((IBinder) r1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onMessageFromNanoApp(int i, short s, NanoAppMessage nanoAppMessage, List<String> list, List<String> list2) {
        if (nanoAppMessage.isBroadcastMessage()) {
            if (!list2.isEmpty()) {
                Log.wtf(TAG, "Received broadcast message with permissions from " + nanoAppMessage.getNanoAppId());
            }
            ContextHubEventLogger.getInstance().logMessageFromNanoapp(i, nanoAppMessage, true);
            broadcastMessage(i, nanoAppMessage, list, list2);
            return;
        }
        ContextHubClientBroker contextHubClientBroker = this.mHostEndPointIdToClientMap.get(Short.valueOf(s));
        if (contextHubClientBroker != null) {
            ContextHubEventLogger.getInstance().logMessageFromNanoapp(i, nanoAppMessage, true);
            contextHubClientBroker.sendMessageToClient(nanoAppMessage, list, list2);
            return;
        }
        ContextHubEventLogger.getInstance().logMessageFromNanoapp(i, nanoAppMessage, false);
        Log.e(TAG, "Cannot send message to unregistered client (host endpoint ID = " + ((int) s) + ")");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterClient(short s) {
        ContextHubClientBroker contextHubClientBroker = this.mHostEndPointIdToClientMap.get(Short.valueOf(s));
        if (contextHubClientBroker != null) {
            this.mRegistrationRecordDeque.add(new RegistrationRecord(contextHubClientBroker.toString(), contextHubClientBroker.isPendingIntentCancelled() ? 2 : 1));
        }
        if (this.mHostEndPointIdToClientMap.remove(Short.valueOf(s)) != null) {
            Log.d(TAG, "Unregistered client with host endpoint ID " + ((int) s));
            return;
        }
        Log.e(TAG, "Cannot unregister non-existing client with host endpoint ID " + ((int) s));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onNanoAppLoaded(int i, final long j) {
        forEachClientOfHub(i, new Consumer() { // from class: com.android.server.location.contexthub.ContextHubClientManager$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ContextHubClientBroker) obj).onNanoAppLoaded(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onNanoAppUnloaded(int i, final long j) {
        forEachClientOfHub(i, new Consumer() { // from class: com.android.server.location.contexthub.ContextHubClientManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ContextHubClientBroker) obj).onNanoAppUnloaded(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onHubReset(int i) {
        forEachClientOfHub(i, new Consumer() { // from class: com.android.server.location.contexthub.ContextHubClientManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ContextHubClientBroker) obj).onHubReset();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onNanoAppAborted(int i, final long j, final int i2) {
        forEachClientOfHub(i, new Consumer() { // from class: com.android.server.location.contexthub.ContextHubClientManager$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ContextHubClientBroker) obj).onNanoAppAborted(j, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forEachClientOfHub(int i, Consumer<ContextHubClientBroker> consumer) {
        for (ContextHubClientBroker contextHubClientBroker : this.mHostEndPointIdToClientMap.values()) {
            if (contextHubClientBroker.getAttachedContextHubId() == i) {
                consumer.accept(contextHubClientBroker);
            }
        }
    }

    private short getHostEndPointId() {
        if (this.mHostEndPointIdToClientMap.size() == 32768) {
            throw new IllegalStateException("Could not register client - max limit exceeded");
        }
        int i = this.mNextHostEndPointId;
        int i2 = 0;
        while (true) {
            if (i2 > MAX_CLIENT_ID) {
                break;
            }
            if (this.mHostEndPointIdToClientMap.containsKey(Short.valueOf((short) i))) {
                i = i == MAX_CLIENT_ID ? 0 : i + 1;
                i2++;
            } else {
                this.mNextHostEndPointId = i != MAX_CLIENT_ID ? i + 1 : 0;
            }
        }
        return (short) i;
    }

    private void broadcastMessage(int i, final NanoAppMessage nanoAppMessage, final List<String> list, final List<String> list2) {
        forEachClientOfHub(i, new Consumer() { // from class: com.android.server.location.contexthub.ContextHubClientManager$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ContextHubClientBroker) obj).sendMessageToClient(nanoAppMessage, list, list2);
            }
        });
    }

    private ContextHubClientBroker getClientBroker(int i, PendingIntent pendingIntent, long j) {
        for (ContextHubClientBroker contextHubClientBroker : this.mHostEndPointIdToClientMap.values()) {
            if (contextHubClientBroker.hasPendingIntent(pendingIntent, j) && contextHubClientBroker.getAttachedContextHubId() == i) {
                return contextHubClientBroker;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(ProtoOutputStream protoOutputStream) {
        for (ContextHubClientBroker contextHubClientBroker : this.mHostEndPointIdToClientMap.values()) {
            long start = protoOutputStream.start(2246267895809L);
            contextHubClientBroker.dump(protoOutputStream);
            protoOutputStream.end(start);
        }
        Iterator<RegistrationRecord> descendingIterator = this.mRegistrationRecordDeque.descendingIterator();
        while (descendingIterator.hasNext()) {
            long start2 = protoOutputStream.start(2246267895810L);
            descendingIterator.next().dump(protoOutputStream);
            protoOutputStream.end(start2);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<ContextHubClientBroker> it = this.mHostEndPointIdToClientMap.values().iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            sb.append(System.lineSeparator());
        }
        sb.append(System.lineSeparator());
        sb.append("Registration History:");
        sb.append(System.lineSeparator());
        Iterator<RegistrationRecord> descendingIterator = this.mRegistrationRecordDeque.descendingIterator();
        while (descendingIterator.hasNext()) {
            sb.append(descendingIterator.next());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
