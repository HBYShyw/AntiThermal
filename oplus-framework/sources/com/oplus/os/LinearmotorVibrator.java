package com.oplus.os;

import android.content.Context;
import android.media.AudioAttributes;
import android.os.CombinedVibration;
import android.os.Debug;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.VibratorManager;
import android.os.vibrator.OplusVibrationEffectSegment;
import android.text.TextUtils;
import android.util.Slog;
import java.util.Collections;

/* loaded from: classes.dex */
public class LinearmotorVibrator {
    private static final int DEPTH_DEBUG_CALLER = 4;
    public static final String FEATURE_WAVEFORM_VIBRATOR = "oplus.software.vibrator_lmvibrator";
    public static final String LINEARMOTORVIBRATOR_SERVICE = "linearmotor";
    public static final String TAG = "LinearmotorVibrator";
    private Context mContext;
    private boolean mLogEnable = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private final ILinearmotorVibratorService mService;
    private VibratorManager mVibratorManager;

    public LinearmotorVibrator(Context context, ILinearmotorVibratorService service) {
        this.mContext = context;
        this.mService = service;
        if (service == null) {
            Slog.v(TAG, "ILinearmotorVibratorService was null");
        }
        this.mVibratorManager = (VibratorManager) context.getSystemService("vibrator_manager");
    }

    public void vibrate(WaveformEffect we) {
        Slog.d(TAG, "vibrate: effect=" + we);
        if (this.mLogEnable) {
            Slog.d(TAG, "vibrate: callers=" + Debug.getCallers(4));
        }
        OplusVibrationEffectSegment segment = null;
        VibrationAttributes attributes = null;
        if (we.getRingtoneVibrateType() == -1) {
            if (we.getEffectType() != -1) {
                segment = new OplusVibrationEffectSegment(we.getEffectType(), we.getStrengthSettingEnabled() ? -1 : we.getEffectStrength());
                attributes = new VibrationAttributes.Builder(new AudioAttributes.Builder().setUsage(we.getUsageHint()).build()).build();
            }
        } else {
            int ringtoneWaveFormEffect = we.getRingtoneVibrateType();
            int strength = we.getStrengthSettingEnabled() ? -1 : we.getEffectStrength();
            attributes = new VibrationAttributes.Builder(new AudioAttributes.Builder().setUsage(we.getUsageHint()).build()).build();
            if (!we.getIsRingtoneCustomized() && !TextUtils.isEmpty(we.getRingtoneFilePath()) && ringtoneWaveFormEffect == 64) {
                segment = new OplusVibrationEffectSegment(we.getRingtoneFilePath(), strength);
            } else if (ringtoneWaveFormEffect != 67) {
                segment = new OplusVibrationEffectSegment(ringtoneWaveFormEffect, strength);
            }
        }
        if (segment != null && attributes != null) {
            VibrationEffect.Composed composed = new VibrationEffect.Composed(Collections.singletonList(segment), we.getEffectLoop() ? 0 : -1);
            composed.validate();
            this.mVibratorManager.vibrate(CombinedVibration.createParallel(composed), attributes);
        }
    }

    public void cancelVibrate(WaveformEffect we) {
        this.mVibratorManager.cancel();
    }

    public int getVibratorStatus() {
        if (this.mService != null) {
            try {
                Slog.d(TAG, "call linearmotor vibrator service getVibratorStatus");
                return this.mService.getVibratorStatus();
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception in LinearmotorVibrator: ", e);
                return -1;
            }
        }
        return -1;
    }

    public void setVibratorStrength(int strength) {
        if (this.mService != null) {
            try {
                Slog.d(TAG, "call linearmotor vibrator service setVibratorStrength");
                this.mService.setVibratorStrength(strength);
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception in LinearmotorVibrator: ", e);
            }
        }
    }

    public int getSettingsTouchEffectStrength() {
        if (this.mService != null) {
            try {
                Slog.d(TAG, "call linearmotor vibrator service getSettingsTouchEffectStrength");
                return this.mService.getSettingsTouchEffectStrength();
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception in LinearmotorVibrator: ", e);
                return 1;
            }
        }
        return 1;
    }

    public int getSettingsRingtoneEffectStrength() {
        if (this.mService != null) {
            try {
                Slog.d(TAG, "call linearmotor vibrator service getSettingsRingtoneEffectStrength");
                return this.mService.getSettingsRingtoneEffectStrength();
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception in LinearmotorVibrator: ", e);
                return 1;
            }
        }
        return 1;
    }

    public int getSettingsNotificationEffectStrength() {
        if (this.mService != null) {
            try {
                Slog.d(TAG, "call linearmotor vibrator service getSettingsNotificationEffectStrength");
                return this.mService.getSettingsNotificationEffectStrength();
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception in LinearmotorVibrator: ", e);
                return 1;
            }
        }
        return 1;
    }

    public int getVibratorTouchStyle() {
        if (this.mService != null) {
            try {
                Slog.d(TAG, "call linearmotor vibrator service getVibratorTouchStyle");
                return this.mService.getVibratorTouchStyle();
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception in LinearmotorVibrator: ", e);
                return -1;
            }
        }
        return -1;
    }

    public void setVibratorTouchStyle(int style) {
        if (this.mService != null) {
            try {
                Slog.d(TAG, "call linearmotor vibrator service setVibratorTouchStyle");
                this.mService.setVibratorTouchStyle(style);
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception in LinearmotorVibrator: ", e);
            }
        }
    }

    public void updateVibrationAmplitude(float amplitudeRatio) {
        if (this.mService != null) {
            try {
                Slog.d(TAG, "call linearmotor vibrator service updateVibrationAmplitude");
                this.mService.updateVibrationAmplitude(amplitudeRatio);
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception in LinearmotorVibrator: ", e);
            }
        }
    }
}
