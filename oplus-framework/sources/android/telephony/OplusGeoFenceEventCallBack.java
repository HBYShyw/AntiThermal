package android.telephony;

import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.IOplusGeoFenceEventCallBack;
import com.android.internal.telephony.IOplusTelephonyExt;

/* loaded from: classes.dex */
public class OplusGeoFenceEventCallBack implements IBinder.DeathRecipient {
    private IOplusGeoFenceEventCallBack mBinder;
    private String mCause;
    private int mCauseMask;
    public String mCustomerName;
    private IBinder mLinkBinder;
    public int mSlot;

    public IOplusGeoFenceEventCallBack getGeoFenceEventCbBinder() {
        if (this.mBinder == null) {
            this.mBinder = new OplusGeoFenceEventCallBackStub();
        }
        return this.mBinder;
    }

    /* loaded from: classes.dex */
    private final class OplusGeoFenceEventCallBackStub extends IOplusGeoFenceEventCallBack.Stub {
        private OplusGeoFenceEventCallBackStub() {
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public int getSlot() throws RemoteException {
            return OplusGeoFenceEventCallBack.this.getSlot();
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public String getCause() throws RemoteException {
            return OplusGeoFenceEventCallBack.this.getCause();
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public String getCustomerName() throws RemoteException {
            return OplusGeoFenceEventCallBack.this.getCustomerName();
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public void setCustomerName(String name) throws RemoteException {
            OplusGeoFenceEventCallBack.this.setCustomerName(name);
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public void onNrDumpFenceEvent(int slotId, int evnet) throws RemoteException {
            OplusGeoFenceEventCallBack.this.onNrDumpFenceEvent(slotId, evnet);
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public void onNrSaDataSlowFenceEvent(int slotId, int evnet) throws RemoteException {
            OplusGeoFenceEventCallBack.this.onNrSaDataSlowFenceEvent(slotId, evnet);
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public void onNrSaWeakFenceEvent(int slotId, int evnet) throws RemoteException {
            OplusGeoFenceEventCallBack.this.onNrSaWeakFenceEvent(slotId, evnet);
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public void onSaGameDelayFenceEvent(int slotId, int evnet) throws RemoteException {
            OplusGeoFenceEventCallBack.this.onSaGameDelayFenceEvent(slotId, evnet);
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public void onLteGameDelayFenceEvent(int slotId, int evnet) throws RemoteException {
            OplusGeoFenceEventCallBack.this.onLteGameDelayFenceEvent(slotId, evnet);
        }

        @Override // android.telephony.IOplusGeoFenceEventCallBack
        public void onSaLteChangeGameDelayFenceEvent(int slotId, int evnet) throws RemoteException {
            OplusGeoFenceEventCallBack.this.onSaLteChangeGameDelayFenceEvent(slotId, evnet);
        }
    }

    public OplusGeoFenceEventCallBack(int slot, int causeMask) {
        this.mCauseMask = 0;
        this.mCause = "NrDump";
        this.mCustomerName = "FenceCustomer";
        this.mSlot = slot;
        this.mCauseMask = causeMask;
    }

    public OplusGeoFenceEventCallBack(int slot, String cause) {
        this.mCauseMask = 0;
        this.mCause = "NrDump";
        this.mCustomerName = "FenceCustomer";
        this.mSlot = slot;
        this.mCause = cause;
    }

    public OplusGeoFenceEventCallBack(int slot, String cause, String customerName) {
        this.mCauseMask = 0;
        this.mCause = "NrDump";
        this.mCustomerName = "FenceCustomer";
        this.mSlot = slot;
        this.mCause = cause;
        this.mCustomerName = customerName;
    }

    public int getSlot() {
        return this.mSlot;
    }

    public String getCause() {
        return this.mCause;
    }

    public String getCustomerName() {
        return this.mCustomerName;
    }

    public void setCustomerName(String name) {
        this.mCustomerName = name;
    }

    public void onNrDumpFenceEvent(int slotId, int evnet) {
    }

    public void onNrSaWeakFenceEvent(int slotId, int event) {
    }

    public void onNrSaDataSlowFenceEvent(int slotId, int event) {
    }

    public void onRemoteBinderDied() {
    }

    public void onSaGameDelayFenceEvent(int slotId, int evnet) {
    }

    public void onLteGameDelayFenceEvent(int slotId, int evnet) {
    }

    public void onSaLteChangeGameDelayFenceEvent(int slotId, int evnet) {
    }

    public void linkToDeath(IOplusTelephonyExt telephonyExt) {
        if (telephonyExt != null) {
            try {
                IBinder asBinder = telephonyExt.asBinder();
                this.mLinkBinder = asBinder;
                asBinder.linkToDeath(this, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void unlinkToDeath() {
        IBinder iBinder = this.mLinkBinder;
        if (iBinder != null) {
            iBinder.unlinkToDeath(this, 0);
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        IBinder iBinder = this.mLinkBinder;
        if (iBinder != null) {
            iBinder.unlinkToDeath(this, 0);
            this.mLinkBinder = null;
        }
        onRemoteBinderDied();
    }

    public String toString() {
        return "{Slot=" + this.mSlot + ",Cause=" + this.mCause + "}";
    }
}
