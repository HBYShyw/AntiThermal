package com.android.server.backup.transport;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.android.server.backup.TransportManager;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class TransportConnectionManager {
    private static final String TAG = "TransportConnectionManager";
    private final Context mContext;
    private final Function<ComponentName, Intent> mIntentFunction;
    private Map<TransportConnection, String> mTransportClientsCallerMap;
    private int mTransportClientsCreated;
    private final Object mTransportClientsLock;
    private final TransportStats mTransportStats;
    private final int mUserId;

    /* JADX INFO: Access modifiers changed from: private */
    public static Intent getRealTransportIntent(ComponentName componentName) {
        return new Intent(TransportManager.SERVICE_ACTION_TRANSPORT_HOST).setComponent(componentName);
    }

    public TransportConnectionManager(int i, Context context, TransportStats transportStats) {
        this(i, context, transportStats, new Function() { // from class: com.android.server.backup.transport.TransportConnectionManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Intent realTransportIntent;
                realTransportIntent = TransportConnectionManager.getRealTransportIntent((ComponentName) obj);
                return realTransportIntent;
            }
        });
    }

    private TransportConnectionManager(int i, Context context, TransportStats transportStats, Function<ComponentName, Intent> function) {
        this.mTransportClientsLock = new Object();
        this.mTransportClientsCreated = 0;
        this.mTransportClientsCallerMap = new WeakHashMap();
        this.mUserId = i;
        this.mContext = context;
        this.mTransportStats = transportStats;
        this.mIntentFunction = function;
    }

    public TransportConnection getTransportClient(ComponentName componentName, String str) {
        return getTransportClient(componentName, (Bundle) null, str);
    }

    public TransportConnection getTransportClient(ComponentName componentName, Bundle bundle, String str) {
        Intent apply = this.mIntentFunction.apply(componentName);
        if (bundle != null) {
            apply.putExtras(bundle);
        }
        return getTransportClient(componentName, str, apply);
    }

    private TransportConnection getTransportClient(ComponentName componentName, String str, Intent intent) {
        TransportConnection transportConnection;
        synchronized (this.mTransportClientsLock) {
            transportConnection = new TransportConnection(this.mUserId, this.mContext, this.mTransportStats, intent, componentName, Integer.toString(this.mTransportClientsCreated), str);
            this.mTransportClientsCallerMap.put(transportConnection, str);
            this.mTransportClientsCreated++;
            TransportUtils.log(3, TAG, TransportUtils.formatMessage(null, str, "Retrieving " + transportConnection));
        }
        return transportConnection;
    }

    public void disposeOfTransportClient(TransportConnection transportConnection, String str) {
        if (transportConnection == null) {
            Log.d(TAG, "caller:" + str);
            return;
        }
        transportConnection.unbind(str);
        transportConnection.markAsDisposed();
        synchronized (this.mTransportClientsLock) {
            TransportUtils.log(3, TAG, TransportUtils.formatMessage(null, str, "Disposing of " + transportConnection));
            this.mTransportClientsCallerMap.remove(transportConnection);
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("Transport clients created: " + this.mTransportClientsCreated);
        synchronized (this.mTransportClientsLock) {
            printWriter.println("Current transport clients: " + this.mTransportClientsCallerMap.size());
            for (TransportConnection transportConnection : this.mTransportClientsCallerMap.keySet()) {
                printWriter.println("    " + transportConnection + " [" + this.mTransportClientsCallerMap.get(transportConnection) + "]");
                Iterator<String> it = transportConnection.getLogBuffer().iterator();
                while (it.hasNext()) {
                    printWriter.println("        " + it.next());
                }
            }
        }
    }
}
