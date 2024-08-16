package com.oplus.epona.ipc.remote;

import android.os.IBinder;
import android.os.RemoteException;
import com.oplus.epona.Dumper;
import com.oplus.epona.ipc.remote.Dispatcher;
import com.oplus.epona.utils.Logger;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class Dispatcher implements Dumper {
    private static final String TAG = "Dispatcher";
    private static volatile Dispatcher sInstance;
    private Map<String, IBinder> mTransferBinders = new ConcurrentHashMap();
    private Map<String, List<String>> mComponents = new ConcurrentHashMap();

    private Dispatcher() {
    }

    public static Dispatcher getInstance() {
        if (sInstance == null) {
            synchronized (Dispatcher.class) {
                if (sInstance == null) {
                    sInstance = new Dispatcher();
                }
            }
        }
        return sInstance;
    }

    private boolean isComponentValid(String str) {
        return (str == null || str.isEmpty()) ? false : true;
    }

    private void printRegisterComponentName(PrintWriter printWriter, List<String> list) {
        if (list == null) {
            return;
        }
        for (String str : list) {
            if (isComponentValid(str)) {
                printWriter.println("    -> " + str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: teardown, reason: merged with bridge method [inline-methods] */
    public void lambda$registerRemoteTransfer$0(String str, String str2) {
        this.mTransferBinders.remove(str);
        this.mComponents.remove(str2);
    }

    private void updateComponent(String str, String str2) {
        List<String> list = this.mComponents.get(str2);
        if (list == null) {
            list = new ArrayList<>();
            this.mComponents.put(str2, list);
        }
        list.add(str);
    }

    @Override // com.oplus.epona.Dumper
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("--- dump package register components info ---");
        for (Map.Entry<String, List<String>> entry : this.mComponents.entrySet()) {
            String key = entry.getKey();
            if (key != null) {
                printWriter.println(key);
                printRegisterComponentName(printWriter, entry.getValue());
            }
        }
        printWriter.println("------------------- end ---------------------");
    }

    public IBinder findRemoteTransfer(String str) {
        return this.mTransferBinders.get(str);
    }

    @Override // com.oplus.epona.Dumper
    public String key() {
        return "oplus_epona";
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0032, code lost:
    
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002f, code lost:
    
        if (r5.mTransferBinders.containsKey(r6) != false) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0033, code lost:
    
        com.oplus.epona.utils.Logger.d(com.oplus.epona.ipc.remote.Dispatcher.TAG, "registerRemoteTransfer: registerSuccess:" + r1, new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0049, code lost:
    
        return r1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean registerRemoteTransfer(final String str, IBinder iBinder, final String str2) {
        boolean z10 = true;
        try {
            try {
                iBinder.linkToDeath(new IBinder.DeathRecipient() { // from class: o6.a
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        Dispatcher.this.lambda$registerRemoteTransfer$0(str, str2);
                    }
                }, 0);
            } catch (RemoteException e10) {
                Logger.w(TAG, e10.toString(), new Object[0]);
            }
        } finally {
            if (!this.mTransferBinders.containsKey(str)) {
                this.mTransferBinders.put(str, iBinder);
                updateComponent(str, str2);
            }
        }
    }
}
