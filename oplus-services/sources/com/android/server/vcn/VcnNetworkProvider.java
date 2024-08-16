package com.android.server.vcn;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkProvider;
import android.net.NetworkRequest;
import android.net.NetworkScore;
import android.net.vcn.VcnGatewayConnectionConfig;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class VcnNetworkProvider extends NetworkProvider {
    private static final String TAG = VcnNetworkProvider.class.getSimpleName();
    private final Context mContext;
    private final Dependencies mDeps;
    private final Handler mHandler;
    private final Set<NetworkRequestListener> mListeners;
    private final Set<NetworkRequest> mRequests;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface NetworkRequestListener {
        void onNetworkRequested(NetworkRequest networkRequest);
    }

    public VcnNetworkProvider(Context context, Looper looper) {
        this(context, looper, new Dependencies());
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public VcnNetworkProvider(Context context, Looper looper, Dependencies dependencies) {
        super(context, looper, TAG);
        Objects.requireNonNull(context, "Missing context");
        Objects.requireNonNull(looper, "Missing looper");
        this.mListeners = new ArraySet();
        this.mRequests = new ArraySet();
        this.mContext = context;
        this.mHandler = new Handler(looper);
        Objects.requireNonNull(dependencies, "Missing dependencies");
        this.mDeps = dependencies;
    }

    public void register() {
        ((ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class)).registerNetworkProvider(this);
        this.mDeps.registerNetworkOffer(this, Vcn.getNetworkScore(), buildCapabilityFilter(), new HandlerExecutor(this.mHandler), new NetworkProvider.NetworkOfferCallback() { // from class: com.android.server.vcn.VcnNetworkProvider.1
            public void onNetworkNeeded(NetworkRequest networkRequest) {
                VcnNetworkProvider.this.handleNetworkRequested(networkRequest);
            }

            public void onNetworkUnneeded(NetworkRequest networkRequest) {
                VcnNetworkProvider.this.handleNetworkRequestWithdrawn(networkRequest);
            }
        });
    }

    private NetworkCapabilities buildCapabilityFilter() {
        NetworkCapabilities.Builder addCapability = new NetworkCapabilities.Builder().addTransportType(0).addCapability(14).addCapability(13).addCapability(15).addCapability(28);
        Iterator it = VcnGatewayConnectionConfig.ALLOWED_CAPABILITIES.iterator();
        while (it.hasNext()) {
            addCapability.addCapability(((Integer) it.next()).intValue());
        }
        return addCapability.build();
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void registerListener(NetworkRequestListener networkRequestListener) {
        this.mListeners.add(networkRequestListener);
        resendAllRequests(networkRequestListener);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void unregisterListener(NetworkRequestListener networkRequestListener) {
        this.mListeners.remove(networkRequestListener);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void resendAllRequests(NetworkRequestListener networkRequestListener) {
        Iterator<NetworkRequest> it = this.mRequests.iterator();
        while (it.hasNext()) {
            notifyListenerForEvent(networkRequestListener, it.next());
        }
    }

    private void notifyListenerForEvent(NetworkRequestListener networkRequestListener, NetworkRequest networkRequest) {
        networkRequestListener.onNetworkRequested(networkRequest);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNetworkRequested(NetworkRequest networkRequest) {
        this.mRequests.add(networkRequest);
        Iterator<NetworkRequestListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            notifyListenerForEvent(it.next(), networkRequest);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNetworkRequestWithdrawn(NetworkRequest networkRequest) {
        this.mRequests.remove(networkRequest);
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("VcnNetworkProvider:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("mListeners:");
        indentingPrintWriter.increaseIndent();
        Iterator<NetworkRequestListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            indentingPrintWriter.println(it.next());
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("mRequests:");
        indentingPrintWriter.increaseIndent();
        Iterator<NetworkRequest> it2 = this.mRequests.iterator();
        while (it2.hasNext()) {
            indentingPrintWriter.println(it2.next());
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.decreaseIndent();
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Dependencies {
        public void registerNetworkOffer(VcnNetworkProvider vcnNetworkProvider, NetworkScore networkScore, NetworkCapabilities networkCapabilities, Executor executor, NetworkProvider.NetworkOfferCallback networkOfferCallback) {
            vcnNetworkProvider.registerNetworkOffer(networkScore, networkCapabilities, executor, networkOfferCallback);
        }
    }
}
