package com.oplus.nfcextns;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Slog;
import com.oplus.nfcextns.INfcExtnsService;

/* loaded from: classes.dex */
public class NfcExtnsManager {
    public static final String OPLUS_DEFAULT_MODEL = "OPLUS-Default";
    private static final String TAG = "NfcExtnsManager";
    private static volatile NfcExtnsManager sNfcExtnsManager = null;
    private Context mContext;
    private INfcExtnsService mService;
    private IBinder mToken = new Binder();

    public NfcExtnsManager(Context context, INfcExtnsService service) {
        this.mService = null;
        this.mContext = context;
        this.mService = service;
        if (service == null) {
            Slog.v(TAG, "mService is null");
        }
    }

    private NfcExtnsManager() {
        this.mService = null;
        Slog.v(TAG, "NfcExtnsManager new default");
        INfcExtnsService asInterface = INfcExtnsService.Stub.asInterface(ServiceManager.getService("nfcextnsservice"));
        this.mService = asInterface;
        if (asInterface == null) {
            Slog.d(TAG, "can not get nfcextnsservice!");
        }
    }

    public static NfcExtnsManager getInstance() {
        Slog.v(TAG, "NfcExtnsManager getInstance enter");
        if (sNfcExtnsManager == null) {
            synchronized (NfcExtnsManager.class) {
                if (sNfcExtnsManager == null) {
                    sNfcExtnsManager = new NfcExtnsManager();
                }
            }
        }
        return sNfcExtnsManager;
    }

    public int oplusPnscrTestGpFelicaSpc() {
        INfcExtnsService iNfcExtnsService = this.mService;
        if (iNfcExtnsService != null) {
            try {
                int spcValue = iNfcExtnsService.oplusPnscrTestGpFelicaSpc();
                return spcValue;
            } catch (RemoteException e) {
                Log.e(TAG, "Remote exception in oplusPnscrTestGpFelicaSpc(): ", e);
                return -1;
            }
        }
        Log.w(TAG, "oplusPnscrTestGpFelicaSpc(): Service not connected!");
        return -1;
    }

    public boolean oplusPnscrTestCardEmulation() {
        INfcExtnsService iNfcExtnsService = this.mService;
        if (iNfcExtnsService != null) {
            try {
                boolean ceResult = iNfcExtnsService.oplusPnscrTestCardEmulation();
                return ceResult;
            } catch (RemoteException e) {
                Log.e(TAG, "Remote exception in oplusPnscrTestCardEmulation(): ", e);
                return false;
            }
        }
        Log.w(TAG, "oplusPnscrTestCardEmulation(): Service not connected!");
        return false;
    }

    public int oplusPnscrGetFreq() {
        INfcExtnsService iNfcExtnsService = this.mService;
        if (iNfcExtnsService != null) {
            try {
                int freqValue = iNfcExtnsService.oplusPnscrGetFreq();
                return freqValue;
            } catch (RemoteException e) {
                Log.e(TAG, "Remote exception in oplusPnscrGetFreq(): ", e);
                return -1;
            }
        }
        Log.w(TAG, "oplusPnscrGetFreq(): Service not connected!");
        return -1;
    }

    public int oplusPnscrGetCurrent() {
        INfcExtnsService iNfcExtnsService = this.mService;
        if (iNfcExtnsService != null) {
            try {
                int currentValue = iNfcExtnsService.oplusPnscrGetCurrent();
                return currentValue;
            } catch (RemoteException e) {
                Log.e(TAG, "Remote exception in oplusPnscrGetCurrent(): ", e);
                return -1;
            }
        }
        Log.w(TAG, "oplusPnscrGetCurrent(): Service not connected!");
        return -1;
    }
}
