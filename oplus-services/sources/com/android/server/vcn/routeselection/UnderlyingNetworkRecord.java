package com.android.server.vcn.routeselection;

import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.vcn.VcnUnderlyingNetworkTemplate;
import android.os.ParcelUuid;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.vcn.TelephonySubscriptionTracker;
import com.android.server.vcn.VcnContext;
import com.android.server.vcn.util.PersistableBundleUtils;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UnderlyingNetworkRecord {
    public final boolean isBlocked;
    public final boolean isSelected;
    public final LinkProperties linkProperties;
    public final Network network;
    public final NetworkCapabilities networkCapabilities;
    public final int priorityClass;

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public UnderlyingNetworkRecord(Network network, NetworkCapabilities networkCapabilities, LinkProperties linkProperties, boolean z, VcnContext vcnContext, List<VcnUnderlyingNetworkTemplate> list, ParcelUuid parcelUuid, TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot, UnderlyingNetworkRecord underlyingNetworkRecord, PersistableBundleUtils.PersistableBundleWrapper persistableBundleWrapper) {
        this.network = network;
        this.networkCapabilities = networkCapabilities;
        this.linkProperties = linkProperties;
        this.isBlocked = z;
        this.isSelected = isSelected(network, underlyingNetworkRecord);
        this.priorityClass = NetworkPriorityClassifier.calculatePriorityClass(vcnContext, this, list, parcelUuid, telephonySubscriptionSnapshot, underlyingNetworkRecord, persistableBundleWrapper);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public UnderlyingNetworkRecord(Network network, NetworkCapabilities networkCapabilities, LinkProperties linkProperties, boolean z, boolean z2, int i) {
        this.network = network;
        this.networkCapabilities = networkCapabilities;
        this.linkProperties = linkProperties;
        this.isBlocked = z;
        this.isSelected = z2;
        this.priorityClass = i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UnderlyingNetworkRecord)) {
            return false;
        }
        UnderlyingNetworkRecord underlyingNetworkRecord = (UnderlyingNetworkRecord) obj;
        return this.network.equals(underlyingNetworkRecord.network) && this.networkCapabilities.equals(underlyingNetworkRecord.networkCapabilities) && this.linkProperties.equals(underlyingNetworkRecord.linkProperties) && this.isBlocked == underlyingNetworkRecord.isBlocked;
    }

    public int hashCode() {
        return Objects.hash(this.network, this.networkCapabilities, this.linkProperties, Boolean.valueOf(this.isBlocked));
    }

    public static boolean isEqualIncludingPriorities(UnderlyingNetworkRecord underlyingNetworkRecord, UnderlyingNetworkRecord underlyingNetworkRecord2) {
        return (underlyingNetworkRecord == null || underlyingNetworkRecord2 == null) ? underlyingNetworkRecord == underlyingNetworkRecord2 : underlyingNetworkRecord.equals(underlyingNetworkRecord2) && underlyingNetworkRecord.isSelected == underlyingNetworkRecord2.isSelected && underlyingNetworkRecord.priorityClass == underlyingNetworkRecord2.priorityClass;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Comparator<UnderlyingNetworkRecord> getComparator() {
        return new Comparator() { // from class: com.android.server.vcn.routeselection.UnderlyingNetworkRecord$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$getComparator$0;
                lambda$getComparator$0 = UnderlyingNetworkRecord.lambda$getComparator$0((UnderlyingNetworkRecord) obj, (UnderlyingNetworkRecord) obj2);
                return lambda$getComparator$0;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$getComparator$0(UnderlyingNetworkRecord underlyingNetworkRecord, UnderlyingNetworkRecord underlyingNetworkRecord2) {
        int i = underlyingNetworkRecord.priorityClass;
        int i2 = underlyingNetworkRecord2.priorityClass;
        if (i == i2) {
            if (underlyingNetworkRecord.isSelected) {
                return -1;
            }
            if (underlyingNetworkRecord2.isSelected) {
                return 1;
            }
        }
        return Integer.compare(i, i2);
    }

    private static boolean isSelected(Network network, UnderlyingNetworkRecord underlyingNetworkRecord) {
        return underlyingNetworkRecord != null && underlyingNetworkRecord.network.equals(network);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(VcnContext vcnContext, IndentingPrintWriter indentingPrintWriter, List<VcnUnderlyingNetworkTemplate> list, ParcelUuid parcelUuid, TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot, UnderlyingNetworkRecord underlyingNetworkRecord, PersistableBundleUtils.PersistableBundleWrapper persistableBundleWrapper) {
        indentingPrintWriter.println("UnderlyingNetworkRecord:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("priorityClass: " + this.priorityClass);
        indentingPrintWriter.println("isSelected: " + this.isSelected);
        indentingPrintWriter.println("mNetwork: " + this.network);
        indentingPrintWriter.println("mNetworkCapabilities: " + this.networkCapabilities);
        indentingPrintWriter.println("mLinkProperties: " + this.linkProperties);
        indentingPrintWriter.decreaseIndent();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class Builder {
        boolean mIsBlocked;
        private LinkProperties mLinkProperties;
        private final Network mNetwork;
        private NetworkCapabilities mNetworkCapabilities;
        boolean mWasIsBlockedSet;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(Network network) {
            this.mNetwork = network;
        }

        Network getNetwork() {
            return this.mNetwork;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setNetworkCapabilities(NetworkCapabilities networkCapabilities) {
            this.mNetworkCapabilities = networkCapabilities;
        }

        NetworkCapabilities getNetworkCapabilities() {
            return this.mNetworkCapabilities;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setLinkProperties(LinkProperties linkProperties) {
            this.mLinkProperties = linkProperties;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setIsBlocked(boolean z) {
            this.mIsBlocked = z;
            this.mWasIsBlockedSet = true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isValid() {
            return (this.mNetworkCapabilities == null || this.mLinkProperties == null || !this.mWasIsBlockedSet) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public UnderlyingNetworkRecord build(VcnContext vcnContext, List<VcnUnderlyingNetworkTemplate> list, ParcelUuid parcelUuid, TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot, UnderlyingNetworkRecord underlyingNetworkRecord, PersistableBundleUtils.PersistableBundleWrapper persistableBundleWrapper) {
            if (!isValid()) {
                throw new IllegalArgumentException("Called build before UnderlyingNetworkRecord was valid");
            }
            return new UnderlyingNetworkRecord(this.mNetwork, this.mNetworkCapabilities, this.mLinkProperties, this.mIsBlocked, vcnContext, list, parcelUuid, telephonySubscriptionSnapshot, underlyingNetworkRecord, persistableBundleWrapper);
        }
    }
}
