package com.oplus.thermalcontrol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.statistics.DataTypeConstants;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import ea.DeepThinkerProxy;
import s6.ThermalFactory;
import s6.ThermalManager;
import u6.ThermalView;
import w4.Affair;
import w4.IAffairCallback;

/* loaded from: classes2.dex */
public class ThermalControlMessage implements IAffairCallback {
    private static final int DEFAULT_STATUS = -100;
    private static final String FLASH_TORCH = "flash_torch";
    private static final String SCREEN_STATUS = "screen_status";
    private static final String SEND_PKG = "send_pkg";
    private static final String SET_CHARGE = "set_charge";
    private static final String SYSTEM_FOLDING_MODE = "system_folding_mode";
    private static final String TAG = "ThermalControlMessage";
    private static volatile ThermalControlMessage sThermalControlMessage;
    private Context mContext;
    private ThermalManager mThermalManager;
    private ThermalControlUtils mUtils = null;

    public ThermalControlMessage(Context context) {
        this.mContext = context;
    }

    public static ThermalControlMessage getInstance(Context context) {
        if (sThermalControlMessage == null) {
            synchronized (ThermalControlMessage.class) {
                if (sThermalControlMessage == null) {
                    sThermalControlMessage = new ThermalControlMessage(context);
                }
            }
        }
        return sThermalControlMessage;
    }

    public void destroy() {
        unregisterAction();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        if (i10 == 205) {
            int intExtra = intent.getIntExtra("plugged", 0);
            if (intent.getIntExtra("Version", 0) != 0) {
                ThermalControlUtils.getInstance(this.mContext).sendChargeStatus(intExtra);
                return;
            }
            return;
        }
        if (i10 == 217) {
            this.mUtils.setIsScreenBlock(false);
            ThermalControlUtils thermalControlUtils = this.mUtils;
            thermalControlUtils.setChargingLevel(thermalControlUtils.getPolicyChargeLevel(), this.mUtils.getSpeedChargeAddLevel(), this.mUtils.getForegroundPkg());
            if (this.mContext.getUserId() == 0) {
                if (this.mUtils.getIsScreenBlockPhoneCall() && !this.mUtils.isPhoneInCall(this.mContext)) {
                    ThermalControllerCenter.getInstance(this.mContext).sendScreenUnLockMessage();
                }
                this.mUtils.setIsScreenBlockPhoneCall(false);
                return;
            }
            return;
        }
        if (i10 == 218) {
            if (this.mUtils.getUserForeground()) {
                DeepThinkerProxy.j(this.mContext).k();
                return;
            } else {
                DeepThinkerProxy.j(this.mContext).h();
                return;
            }
        }
        if (i10 == 225) {
            this.mUtils.setUserForeground(false);
            ThermalView.g(false);
            DeepThinkerProxy.j(this.mContext).h();
            this.mThermalManager.w(false);
            return;
        }
        if (i10 == 226) {
            this.mUtils.setUserForeground(true);
            ThermalView.g(true);
            DeepThinkerProxy.j(this.mContext).k();
            this.mThermalManager.w(true);
            Settings.System.putIntForUser(this.mContext.getContentResolver(), "oplus_settings_hightemp_safety_state", Settings.System.getIntForUser(this.mContext.getContentResolver(), "oplus_settings_hightemp_safety_state", 0, 0), this.mContext.getUserId());
            Settings.System.putIntForUser(this.mContext.getContentResolver(), "oplus_settings_hightemp_protect", Settings.System.getIntForUser(this.mContext.getContentResolver(), "oplus_settings_hightemp_protect", 0, 0), this.mContext.getUserId());
            ThermalControllerCenter.getInstance(this.mContext).sendSceneChangeMessage();
            return;
        }
        if (i10 == 310) {
            if (this.mUtils.getUserForeground()) {
                this.mUtils.sendScreenStatus(true);
                return;
            }
            return;
        }
        if (i10 == 311) {
            if (this.mUtils.getUserForeground()) {
                this.mUtils.sendScreenStatus(false);
                return;
            }
            return;
        }
        char c10 = 65535;
        if (i10 == 1210) {
            if (this.mUtils.getUserForeground()) {
                LocalLog.a(TAG, "third = " + intent.getIntExtra("third_step", 0) + " in user " + this.mContext.getUserId());
                if (intent.getIntExtra("third_step", 0) == 1) {
                    this.mThermalManager.f18120a.sendEmptyMessage(DataTypeConstants.PERIOD_DATA);
                    return;
                } else {
                    if (intent.getIntExtra("third_step", 0) == -1) {
                        this.mThermalManager.f18120a.sendEmptyMessage(DataTypeConstants.SETTING_KEY);
                        return;
                    }
                    return;
                }
            }
            return;
        }
        if (i10 != 1211) {
            switch (i10) {
                case 1201:
                    if (this.mUtils.getUserForeground()) {
                        LocalLog.a(TAG, "first = " + intent.getIntExtra("first_step", 0) + " in user " + this.mContext.getUserId());
                        if (intent.getIntExtra("first_step", 0) == 1) {
                            this.mThermalManager.f18120a.sendEmptyMessage(1011);
                            return;
                        } else {
                            if (intent.getIntExtra("first_step", 0) == -1) {
                                this.mThermalManager.f18120a.sendEmptyMessage(DataTypeConstants.PAGE_VISIT);
                                return;
                            }
                            return;
                        }
                    }
                    return;
                case 1202:
                    if (this.mUtils.getUserForeground()) {
                        LocalLog.a(TAG, "second = " + intent.getIntExtra("second_step", 0) + " in user " + this.mContext.getUserId());
                        if (intent.getIntExtra("second_step", 0) == 1) {
                            this.mThermalManager.f18120a.sendEmptyMessage(1012);
                            return;
                        } else {
                            if (intent.getIntExtra("second_step", 0) == -1) {
                                this.mThermalManager.f18120a.sendEmptyMessage(DataTypeConstants.SPECIAL_APP_START);
                                return;
                            }
                            return;
                        }
                    }
                    return;
                case 1203:
                    String stringExtra = intent.getStringExtra("option");
                    int intExtra2 = intent.getIntExtra("status", DEFAULT_STATUS);
                    LocalLog.a(TAG, "option = " + stringExtra + " status = " + intExtra2);
                    if (intExtra2 != DEFAULT_STATUS) {
                        Settings.System.putInt(this.mContext.getContentResolver(), stringExtra, intExtra2);
                        return;
                    }
                    return;
                case 1204:
                    String stringExtra2 = intent.getStringExtra("name");
                    LocalLog.a(TAG, "name = " + stringExtra2);
                    stringExtra2.hashCode();
                    switch (stringExtra2.hashCode()) {
                        case -2052673807:
                            if (stringExtra2.equals(SYSTEM_FOLDING_MODE)) {
                                c10 = 0;
                                break;
                            }
                            break;
                        case -1639116495:
                            if (stringExtra2.equals(SET_CHARGE)) {
                                c10 = 1;
                                break;
                            }
                            break;
                        case 1247784085:
                            if (stringExtra2.equals(SEND_PKG)) {
                                c10 = 2;
                                break;
                            }
                            break;
                        case 1532951333:
                            if (stringExtra2.equals(SCREEN_STATUS)) {
                                c10 = 3;
                                break;
                            }
                            break;
                        case 1617654509:
                            if (stringExtra2.equals(FLASH_TORCH)) {
                                c10 = 4;
                                break;
                            }
                            break;
                    }
                    switch (c10) {
                        case 0:
                            if (this.mContext.getUserId() == 0) {
                                this.mUtils.sendSystemFoldingMode(intent.getIntExtra("state", 0));
                                return;
                            }
                            return;
                        case 1:
                            if (this.mContext.getUserId() == 0) {
                                this.mUtils.setIsScreenBlock(intent.getBooleanExtra("isScreenBlock", false));
                                this.mUtils.setChargingLevel(intent.getIntExtra("level", 0), intent.getIntExtra(ThermalPolicy.KEY_SPEED_CHARGE_ADD, 0), intent.getStringExtra("foregroundPkgName"));
                                return;
                            }
                            return;
                        case 2:
                            if (this.mContext.getUserId() == 0) {
                                this.mUtils.sendPkgName(intent.getStringExtra("pkgName"));
                                return;
                            }
                            return;
                        case 3:
                            if (this.mContext.getUserId() == 0) {
                                this.mUtils.sendScreenStatus(intent.getBooleanExtra("isScreenOn", true));
                                return;
                            }
                            return;
                        case 4:
                            if (this.mContext.getUserId() == 0) {
                                intent.getBooleanExtra("state", false);
                                this.mUtils.sendFlashTorch(false);
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                default:
                    return;
            }
        }
        Bundle extras = intent.getExtras();
        if (extras == null || !this.mUtils.getUserForeground()) {
            return;
        }
        LocalLog.a(TAG, "disValue = " + extras.getInt("close_policy", 0) + " in user " + this.mContext.getUserId());
        Message obtain = Message.obtain();
        if (extras.getInt("close_policy", 0) != 0) {
            obtain.setData(extras);
            obtain.what = 1024;
            this.mThermalManager.f18120a.sendMessage(obtain);
        }
    }

    public void init(Looper looper) {
        this.mUtils = ThermalControlUtils.getInstance(this.mContext);
        this.mThermalManager = ThermalFactory.a(this.mContext, looper);
        registerAction();
        LocalLog.a(TAG, "thermal message init");
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 311);
        Affair.f().g(this, 310);
        Affair.f().g(this, EventType.SCENE_MODE_VIDEO);
        Affair.f().g(this, 225);
        Affair.f().g(this, 226);
        Affair.f().g(this, EventType.SCENE_MODE_LEARNING);
        Affair.f().g(this, EventType.SCENE_MODE_NAVIGATION);
        Affair.f().g(this, 1201);
        Affair.f().g(this, 1202);
        Affair.f().g(this, 1210);
        Affair.f().g(this, 1211);
        Affair.f().g(this, 1204);
        Affair.f().g(this, 1203);
        Affair.f().g(this, 901);
        Affair.f().g(this, 903);
        Affair.f().g(this, 902);
    }

    public void unregisterAction() {
        Affair.f().i(this, 311);
        Affair.f().i(this, 310);
        Affair.f().i(this, EventType.SCENE_MODE_VIDEO);
        Affair.f().i(this, 225);
        Affair.f().i(this, 226);
        Affair.f().i(this, EventType.SCENE_MODE_LEARNING);
        Affair.f().i(this, EventType.SCENE_MODE_NAVIGATION);
        Affair.f().i(this, 1201);
        Affair.f().i(this, 1202);
        Affair.f().i(this, 1210);
        Affair.f().i(this, 1211);
        Affair.f().i(this, 1204);
        Affair.f().i(this, 1203);
        Affair.f().i(this, 901);
        Affair.f().i(this, 903);
        Affair.f().i(this, 902);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
        switch (i10) {
            case 901:
                this.mUtils.setPowersaveMode(bundle.getBoolean("powersave_state"));
                return;
            case 902:
                this.mUtils.setHighPrefMode(bundle.getBoolean("highpref_state"));
                return;
            case 903:
                if (this.mContext.getUserId() == 0) {
                    this.mUtils.setSuperPowerSaveMode(bundle.getBoolean("s_powersave_state"));
                    return;
                }
                return;
            default:
                return;
        }
    }
}
