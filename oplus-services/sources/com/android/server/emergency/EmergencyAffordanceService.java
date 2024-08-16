package com.android.server.emergency;

import android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.SystemService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class EmergencyAffordanceService extends SystemService {
    private static final boolean DBG = false;
    private static final String EMERGENCY_AFFORDANCE_OVERRIDE_ISO = "emergency_affordance_override_iso";
    private static final int INITIALIZE_STATE = 1;
    private static final int NETWORK_COUNTRY_CHANGED = 2;
    private static final String SERVICE_NAME = "emergency_affordance";
    private static final int SUBSCRIPTION_CHANGED = 3;
    private static final String TAG = "EmergencyAffordanceService";
    private static final int UPDATE_AIRPLANE_MODE_STATUS = 4;
    private boolean mAirplaneModeEnabled;
    private boolean mAnyNetworkNeedsEmergencyAffordance;
    private boolean mAnySimNeedsEmergencyAffordance;
    private BroadcastReceiver mBroadcastReceiver;
    private final Context mContext;
    private boolean mEmergencyAffordanceNeeded;
    private final ArrayList<String> mEmergencyCallCountryIsos;
    private MyHandler mHandler;
    private SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionChangedListener;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;
    private boolean mVoiceCapable;

    public EmergencyAffordanceService(Context context) {
        super(context);
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.emergency.EmergencyAffordanceService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("android.telephony.action.NETWORK_COUNTRY_CHANGED".equals(intent.getAction())) {
                    String stringExtra = intent.getStringExtra("android.telephony.extra.NETWORK_COUNTRY");
                    EmergencyAffordanceService.this.mHandler.obtainMessage(2, intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1), 0, stringExtra).sendToTarget();
                } else if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                    EmergencyAffordanceService.this.mHandler.obtainMessage(4).sendToTarget();
                }
            }
        };
        this.mSubscriptionChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.server.emergency.EmergencyAffordanceService.2
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                EmergencyAffordanceService.this.mHandler.obtainMessage(3).sendToTarget();
            }
        };
        this.mContext = context;
        String[] stringArray = context.getResources().getStringArray(R.array.config_oemUsbModeOverride);
        this.mEmergencyCallCountryIsos = new ArrayList<>(stringArray.length);
        for (String str : stringArray) {
            this.mEmergencyCallCountryIsos.add(str);
        }
        if (Build.IS_DEBUGGABLE) {
            String string = Settings.Global.getString(this.mContext.getContentResolver(), EMERGENCY_AFFORDANCE_OVERRIDE_ISO);
            if (TextUtils.isEmpty(string)) {
                return;
            }
            this.mEmergencyCallCountryIsos.clear();
            this.mEmergencyCallCountryIsos.add(string);
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService(SERVICE_NAME, new BinderService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        if (i == 600) {
            handleThirdPartyBootPhase();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                EmergencyAffordanceService.this.handleInitializeState();
                return;
            }
            if (i == 2) {
                EmergencyAffordanceService.this.handleNetworkCountryChanged((String) message.obj, message.arg1);
            } else {
                if (i == 3) {
                    EmergencyAffordanceService.this.handleUpdateSimSubscriptionInfo();
                    return;
                }
                if (i == 4) {
                    EmergencyAffordanceService.this.handleUpdateAirplaneModeStatus();
                    return;
                }
                Slog.e(EmergencyAffordanceService.TAG, "Unexpected message received: " + message.what);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInitializeState() {
        handleUpdateAirplaneModeStatus();
        handleUpdateSimSubscriptionInfo();
        updateNetworkCountry();
        updateEmergencyAffordanceNeeded();
    }

    private void handleThirdPartyBootPhase() {
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        this.mTelephonyManager = telephonyManager;
        boolean isVoiceCapable = telephonyManager.isVoiceCapable();
        this.mVoiceCapable = isVoiceCapable;
        if (!isVoiceCapable) {
            updateEmergencyAffordanceNeeded();
            return;
        }
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        this.mHandler = new MyHandler(handlerThread.getLooper());
        SubscriptionManager from = SubscriptionManager.from(this.mContext);
        this.mSubscriptionManager = from;
        from.addOnSubscriptionsChangedListener(this.mSubscriptionChangedListener);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.telephony.action.NETWORK_COUNTRY_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter);
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUpdateAirplaneModeStatus() {
        this.mAirplaneModeEnabled = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUpdateSimSubscriptionInfo() {
        boolean z;
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList == null) {
            return;
        }
        Iterator<SubscriptionInfo> it = activeSubscriptionInfoList.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            } else if (isoRequiresEmergencyAffordance(it.next().getCountryIso())) {
                z = true;
                break;
            }
        }
        this.mAnySimNeedsEmergencyAffordance = z;
        updateEmergencyAffordanceNeeded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNetworkCountryChanged(String str, int i) {
        if (TextUtils.isEmpty(str) && this.mAirplaneModeEnabled) {
            Slog.w(TAG, "Ignore empty countryIso report when APM is on.");
        } else {
            updateNetworkCountry();
            updateEmergencyAffordanceNeeded();
        }
    }

    private void updateNetworkCountry() {
        int activeModemCount = this.mTelephonyManager.getActiveModemCount();
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= activeModemCount) {
                break;
            }
            if (isoRequiresEmergencyAffordance(this.mTelephonyManager.getNetworkCountryIso(i))) {
                z = true;
                break;
            }
            i++;
        }
        this.mAnyNetworkNeedsEmergencyAffordance = z;
        updateEmergencyAffordanceNeeded();
    }

    private boolean isoRequiresEmergencyAffordance(String str) {
        return this.mEmergencyCallCountryIsos.contains(str);
    }

    private void updateEmergencyAffordanceNeeded() {
        boolean z = this.mEmergencyAffordanceNeeded;
        boolean z2 = this.mVoiceCapable && (this.mAnySimNeedsEmergencyAffordance || this.mAnyNetworkNeedsEmergencyAffordance);
        this.mEmergencyAffordanceNeeded = z2;
        if (z != z2) {
            Settings.Global.putInt(this.mContext.getContentResolver(), "emergency_affordance_needed", this.mEmergencyAffordanceNeeded ? 1 : 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpInternal(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("EmergencyAffordanceService (dumpsys emergency_affordance) state:\n");
        indentingPrintWriter.println("mEmergencyAffordanceNeeded=" + this.mEmergencyAffordanceNeeded);
        indentingPrintWriter.println("mVoiceCapable=" + this.mVoiceCapable);
        indentingPrintWriter.println("mAnySimNeedsEmergencyAffordance=" + this.mAnySimNeedsEmergencyAffordance);
        indentingPrintWriter.println("mAnyNetworkNeedsEmergencyAffordance=" + this.mAnyNetworkNeedsEmergencyAffordance);
        indentingPrintWriter.println("mEmergencyCallCountryIsos=" + String.join(",", this.mEmergencyCallCountryIsos));
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class BinderService extends Binder {
        private BinderService() {
        }

        @Override // android.os.Binder
        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(EmergencyAffordanceService.this.mContext, EmergencyAffordanceService.TAG, printWriter)) {
                EmergencyAffordanceService.this.dumpInternal(new IndentingPrintWriter(printWriter, "  "));
            }
        }
    }
}
