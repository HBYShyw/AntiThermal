package android.os;

import android.content.Context;
import android.util.Log;
import com.oplus.os.ILinearmotorVibratorService;
import com.oplus.os.LinearmotorVibrator;

/* loaded from: classes.dex */
public class SystemVibratorExtImpl implements ISystemVibratorExt {
    private static final String TAG = "SystemVibratorExtImpl";
    private ILinearmotorVibratorService mLMService;
    private Vibrator mSystemVibrator;

    public SystemVibratorExtImpl(Object base) {
        this.mSystemVibrator = (Vibrator) base;
    }

    public void init(Context context) {
        this.mLMService = ILinearmotorVibratorService.Stub.asInterface(ServiceManager.getService(LinearmotorVibrator.LINEARMOTORVIBRATOR_SERVICE));
    }

    public int getVibratorStatus() {
        ILinearmotorVibratorService iLinearmotorVibratorService = this.mLMService;
        if (iLinearmotorVibratorService == null) {
            Log.w(TAG, "Failed to getVibratorStatus; no vibrator service.");
            return -1;
        }
        try {
            return iLinearmotorVibratorService.getVibratorStatus();
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to getVibratorStatus.", e);
            return -1;
        }
    }

    public void setVibratorStrength(int strength) {
        ILinearmotorVibratorService iLinearmotorVibratorService = this.mLMService;
        if (iLinearmotorVibratorService == null) {
            Log.w(TAG, "Failed to setVibratorStrength; no vibrator service.");
            return;
        }
        try {
            iLinearmotorVibratorService.setVibratorStrength(strength);
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to setVibratorStrength.", e);
        }
    }

    public int getVibratorTouchStyle() {
        ILinearmotorVibratorService iLinearmotorVibratorService = this.mLMService;
        if (iLinearmotorVibratorService == null) {
            Log.w(TAG, "Failed to getVibratorTouchStyle; no vibrator service.");
            return -1;
        }
        try {
            return iLinearmotorVibratorService.getVibratorTouchStyle();
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to getVibratorTouchStyle.", e);
            return -1;
        }
    }

    public void setVibratorTouchStyle(int style) {
        ILinearmotorVibratorService iLinearmotorVibratorService = this.mLMService;
        if (iLinearmotorVibratorService == null) {
            Log.w(TAG, "Failed to setVibratorTouchStyle; no vibrator service.");
            return;
        }
        try {
            iLinearmotorVibratorService.setVibratorTouchStyle(style);
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to setVibratorTouchStyle.", e);
        }
    }
}
