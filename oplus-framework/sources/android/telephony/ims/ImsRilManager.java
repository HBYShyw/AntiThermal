package android.telephony.ims;

import android.content.Context;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsRilManager;
import android.telephony.ims.aidl.IImsRil;
import android.telephony.ims.aidl.IImsRilInd;
import android.util.Log;
import com.oplus.ims.IImsExt;
import java.util.ArrayList;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class ImsRilManager {
    private final String TAG;
    private ArrayList<ICleanupListener> mCleanupListeners;
    private Context mContext;
    private IImsExt mIImsExt;
    private IImsRil mImsRil;
    private int numPhoens;

    /* loaded from: classes.dex */
    public interface ICleanupListener {
        void dispose();
    }

    public void addCleanupListener(ICleanupListener listener) {
        if (listener != null) {
            this.mCleanupListeners.add(listener);
        }
    }

    public void removeCleanupListener(ICleanupListener listener) {
        if (listener != null) {
            this.mCleanupListeners.remove(listener);
        }
    }

    public ImsRilManager(Context context) {
        this(context, null);
    }

    public ImsRilManager(Context context, IImsRil rilMgr) {
        this.TAG = "ImsRilManager";
        this.mCleanupListeners = new ArrayList<>();
        this.mContext = context;
        this.mImsRil = rilMgr;
        this.numPhoens = TelephonyManager.from(context).getPhoneCount();
    }

    void validateInvariants(int phoneId) throws Exception {
        checkBinder();
        checkPhoneId(phoneId);
    }

    private void checkBinder() throws Exception {
        if (this.mImsRil == null) {
            throw new Exception("ImsRil Service is not running");
        }
    }

    private void checkPhoneId(int phoneId) throws Exception {
        if (phoneId < 0 || phoneId >= this.numPhoens) {
            Log.i("ImsRilManager", "phoneId " + phoneId + " is not valid");
            throw new Exception("invalid phoneId");
        }
    }

    public void dispose() {
        this.mCleanupListeners.forEach(new Consumer() { // from class: android.telephony.ims.ImsRilManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ImsRilManager.ICleanupListener) obj).dispose();
            }
        });
        this.mImsRil = null;
        this.mContext = null;
        this.mIImsExt = null;
    }

    public void setImsExt(IImsExt iImsExt) {
        this.mIImsExt = iImsExt;
    }

    public IImsExt getImsExt() {
        return this.mIImsExt;
    }

    public void commonReqToIms(int phoneId, int requestId, Message result) throws Exception {
        validateInvariants(phoneId);
        this.mImsRil.commonReqToIms(phoneId, requestId, result);
    }

    public void registerIndication(IImsRilInd ind) throws Exception {
        this.mImsRil.registerIndication(ind);
    }

    public void unRegisterIndication(IImsRilInd ind) throws Exception {
        this.mImsRil.unRegisterIndication(ind);
    }
}
