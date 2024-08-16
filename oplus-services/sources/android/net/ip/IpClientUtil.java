package android.net.ip;

import android.content.Context;
import android.net.DhcpResultsParcelable;
import android.net.Layer2PacketParcelable;
import android.net.LinkProperties;
import android.net.ip.IIpClientCallbacks;
import android.net.networkstack.ModuleNetworkStackClient;
import android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable;
import android.os.ConditionVariable;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class IpClientUtil {
    public static final String DUMP_ARG = "ipclient";

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class WaitForProvisioningCallbacks extends IpClientCallbacks {
        private final ConditionVariable mCV = new ConditionVariable();
        private LinkProperties mCallbackLinkProperties;

        public LinkProperties waitForProvisioning() {
            this.mCV.block();
            return this.mCallbackLinkProperties;
        }

        @Override // android.net.ip.IpClientCallbacks
        public void onProvisioningSuccess(LinkProperties linkProperties) {
            this.mCallbackLinkProperties = linkProperties;
            this.mCV.open();
        }

        @Override // android.net.ip.IpClientCallbacks
        public void onProvisioningFailure(LinkProperties linkProperties) {
            this.mCallbackLinkProperties = null;
            this.mCV.open();
        }
    }

    public static void makeIpClient(Context context, String str, IpClientCallbacks ipClientCallbacks) {
        ModuleNetworkStackClient.getInstance(context).makeIpClient(str, new IpClientCallbacksProxy(ipClientCallbacks));
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static class IpClientCallbacksProxy extends IIpClientCallbacks.Stub {
        protected final IpClientCallbacks mCb;

        @Override // android.net.ip.IIpClientCallbacks
        public String getInterfaceHash() {
            return "4d26968d0f6cb11c9bb669a3f8ebc7a1c39f9391";
        }

        @Override // android.net.ip.IIpClientCallbacks
        public int getInterfaceVersion() {
            return 18;
        }

        IpClientCallbacksProxy(IpClientCallbacks ipClientCallbacks) {
            this.mCb = ipClientCallbacks;
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onIpClientCreated(IIpClient iIpClient) {
            this.mCb.onIpClientCreated(iIpClient);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onPreDhcpAction() {
            this.mCb.onPreDhcpAction();
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onPostDhcpAction() {
            this.mCb.onPostDhcpAction();
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onNewDhcpResults(DhcpResultsParcelable dhcpResultsParcelable) {
            this.mCb.onNewDhcpResults(dhcpResultsParcelable);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onProvisioningSuccess(LinkProperties linkProperties) {
            this.mCb.onProvisioningSuccess(new LinkProperties(linkProperties));
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onProvisioningFailure(LinkProperties linkProperties) {
            this.mCb.onProvisioningFailure(new LinkProperties(linkProperties));
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onLinkPropertiesChange(LinkProperties linkProperties) {
            this.mCb.onLinkPropertiesChange(new LinkProperties(linkProperties));
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onReachabilityLost(String str) {
            this.mCb.onReachabilityLost(str);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onQuit() {
            this.mCb.onQuit();
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void installPacketFilter(byte[] bArr) {
            this.mCb.installPacketFilter(bArr);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void startReadPacketFilter() {
            this.mCb.startReadPacketFilter();
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void setFallbackMulticastFilter(boolean z) {
            this.mCb.setFallbackMulticastFilter(z);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void setNeighborDiscoveryOffload(boolean z) {
            this.mCb.setNeighborDiscoveryOffload(z);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onPreconnectionStart(List<Layer2PacketParcelable> list) {
            this.mCb.onPreconnectionStart(list);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onReachabilityFailure(ReachabilityLossInfoParcelable reachabilityLossInfoParcelable) {
            this.mCb.onReachabilityFailure(reachabilityLossInfoParcelable);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void setMaxDtimMultiplier(int i) {
            this.mCb.setMaxDtimMultiplier(i);
        }
    }

    public static void dumpIpClient(IIpClient iIpClient, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("IpClient logs have moved to dumpsys network_stack");
    }
}
